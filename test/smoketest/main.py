#!/usr/bin/env python
"""Smoketest the RLA server
Run thru a simple server_sequence.

TODO: display errors nicely.
"""

from __future__ import print_function
import sys
import requests
from server_test import *


def state_login(baseurl, s):
    "Login as state admin in given requests session"

    path = "/auth-state-admin"
    r = s.post(baseurl + path,
               data={'username': 'stateadmin1', 'password': '', 'second_factor': ''})
    print(r, path)


def county_login(baseurl, s, county_id):
    "Login as county admin in given requests session"

    path = "/auth-county-admin"
    r = s.post(baseurl + path,
               data={'username': 'countyadmin%d' % county_id, 'password': '', 'second_factor': ''})
    print(r, path)


def test_endpoint_json(baseurl, s, path, data):
    "Do a generic test of an endpoint that posts the given data to the given path"

    r = s.post(baseurl + path, json=data)
    print(r, path, r.text)
    return r


def test_endpoint_get(baseurl, s, path):
    "Do a generic test of an endpoint that gets the given path"

    r = s.get(baseurl + path)
    print(r, path, r.text)
    return r


def test_endpoint_bytes(baseurl, s, path, data):
    "Do a generic test of an endpoint that posts the given data to the given path"

    r = s.post(baseurl + path, data)
    print(r, path, r.text)
    return r


def test_endpoint_post(baseurl, s, path, data):
    "Do a generic test of an endpoint that posts the given data to the given path"

    r = s.post(baseurl + path, data)
    print(r, path, r.text)
    return r


def upload_cvrs(baseurl, s, filename, sha256):
    "Upload cvrs"

    with open(filename, 'rb') as f:
        path = "/upload-cvr-export"
        payload = {'county': '3', 'hash': sha256}
        r = s.post(baseurl + path,
                          files={'cvr_file': f}, data=payload)
    print(r, path)


def upload_manifest(baseurl, s, filename, sha256):
    "Upload manifest"

    with open(filename, 'rb') as f:
        path = "/upload-ballot-manifest"
        payload = {'county': 'Arapahoe', 'hash': sha256}
        r = s.post(baseurl + path,
                          files={'bmi_file': f}, data=payload)
    print(r, path)


def upload_acvr(baseurl, s, filename):
    "Upload audit CVR (acvr)"

    with open(filename, 'rb') as f:
        path = "/upload-audit-cvr"
        r = s.post(baseurl + path,
                          files={'audit_cvr': f})
    print(r, path)


def upload_files(baseurl, s):
    """Directly upload files, which zerotest doesn't support.
    See "File upload via POST request not working: Issue #12"
     https://github.com/jjyr/zerotest/issues/12
    """

    upload_manifest(baseurl, s, "../e-1/arapahoe-manifest.csv",
               "42d409d3394243046cf92e3ce569b7078cba0815d626602d15d0da3e5e844a94")
    upload_cvrs(baseurl, s, "../e-1/arapahoe-regent-3-clear-CVR_Export.csv",
               "413befb5bc3e577e637bd789a92da425d0309310c51cfe796e57c01a1987f4bf")


def server_sequence():
    '''Run thru a given test sequence to explore server ASM transitions.
    TODO: needs lots of work to easily handle a full Eulerian traversal
    of all transitions in the state graphs.
    '''

    test_get_cvr()
    test_get_ballot_manifest()
    test_get_contest()
    test_get_cvr_county_Arapahoe()
    test_get_ballot_manifest_county_Arapahoe()
    test_get_contest_id_2()
    test_get_contest_county_Arapahoe()

if __name__ == "__main__":
    # When we're updating the tests themselves, the server is running
    # on port 8887.

    if len(sys.argv) > 1  and  sys.argv[1] == "--update":
        base = "http://localhost:8887"
    else:
        base = "http://localhost:8888"

    state_s = requests.Session()
    state_login(base, state_s)

    county_s1 = requests.Session()
    county_login(base, county_s1, 3)

    r = test_endpoint_json(base, state_s, "/risk-limit-comp-audits", {"risk_limit": 0.1})

    # Alternatives that work, FWIW
    # r = test_endpoint_post(base, state_s, "/risk-limit-comp-audits", '{"risk_limit": "0.1"}')
    # r = test_endpoint_bytes(base, state_s, "/risk-limit-comp-audits", '{"risk_limit": "0.1"}')

    r = test_endpoint_json(base, county_s1, "/audit-board",
                           [{"first_name": "Mary",
                             "last_name": "Doe",
                             "political_party": "Democrat"},
                            {"first_name": "John",
                             "last_name": "Doe",
                             "political_party": "Republican"}])

    upload_files(base, county_s1)

    # Replace that with this later - or make test_endpoint_file method?
    # r = test_endpoint_post(base, county_s1, "/upload-ballot-manifest", ...)
    # r = test_endpoint_post(base, county_s1, "/upload-cvr-export", ...)

    # We need a valid contest to audit. Pick the first one.
    r = test_endpoint_get(base, county_s1, "/contest")
    contests = r.json()
    print(contests)
    contest_to_audit = contests[0]['id']

    r = test_endpoint_json(base, state_s, "/select-contests",
                           [{"contest": contest_to_audit,
                             "reason": "COUNTY_WIDE_CONTEST",
                             "audit": "COMPARISON"}])

    r = test_endpoint_get(base, state_s, "/publish-data-to-audit")

    r = test_endpoint_json(base, state_s, "/random-seed",
                           {'seed': "01234567890123456789"})
    r = test_endpoint_json(base, state_s, "/ballots-to-audit", {})
    
    r = test_endpoint_get(base, county_s1, "/county-dashboard")
    # r = test_endpoint_get(base, county_s1, "/audit-board-asm-state")
    # r = test_endpoint_json(base, county_s1, "/audit-board-dashboard", {})
    upload_acvr(base, county_s1, "acvr.json")

    r = test_endpoint_get(base, county_s1, "/county-dashboard")

    # r = test_endpoint_json(base, county_s1, "/upload-audit-cvr", {})

    # LOOP

    r = test_endpoint_json(base, county_s1, "/intermediate-audit-report", {})
    r = test_endpoint_json(base, county_s1, "/audit-report", {})

    r = test_endpoint_json(base, state_s, "/publish-report", {})

    # server_sequence()
