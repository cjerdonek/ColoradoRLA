import * as React from 'react';
import { connect } from 'react-redux';

import AuditSeedPage from './AuditSeedPage';


class AuditSeedContainer extends React.Component<any, any> {
    public render() {
        const back = () => this.props.history.push('/sos/audit');
        const saveAndNext = () => this.props.history.push('/sos/audit/ballots');

        return <AuditSeedPage { ...{ back, saveAndNext } } />;
    }
}

const mapStateToProps = () => ({});

const mapDispatchToProps = (dispatch: any) => ({});

export default connect(
    mapStateToProps,
    mapDispatchToProps,
)(AuditSeedContainer);
