import * as React from 'react';
import { connect } from 'react-redux';
import { bindActionCreators } from 'redux';

import BallotAuditStage from './BallotAuditStage';

import findById from '../../../../findById';


class BallotAuditStageContainer extends React.Component<any, any> {
    public render() {
        return <BallotAuditStage { ...this.props } />;
    }
}

const mapStateToProps = (state: any) => {
    const { ballotStyles, county } = state;
    const { ballots, currentBallotId } = county;

    const currentBallot = findById(ballots, currentBallotId);

    return { ballotStyles, county, currentBallot, marks: currentBallot.marks };
};

const mapDispatchToProps = (dispatch: any) => bindActionCreators({
    updateBallotMarks: (data: any) => ({
        data,
        type: 'UPDATE_BALLOT_MARKS',
    }),
}, dispatch);

export default connect(
    mapStateToProps,
    mapDispatchToProps,
)(BallotAuditStageContainer);
