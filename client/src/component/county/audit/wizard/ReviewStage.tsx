import * as React from 'react';

import * as _ from 'lodash';

import findById from '../../../../findById';

import BackButton from './BackButton';


const BallotContestResultVoteForN = () => (
    <div className='pt-card'>
        <div className='pt-card'>
            <div>Ballot contest 1</div>
            <div>Acme County School District RE-1</div>
            <div>Director</div>
        </div>
        <div className='pt-card'>
            <div>Choice B</div>
            <div>Choice C</div>
        </div>
    </div>
);

const BallotContestResultYesNo = () => (
    <div className='pt-card'>
        <div className='pt-card'>
            <div>Ballot contest 2</div>
            <div>Prop 101</div>
        </div>
        <div className='pt-card'>
            Yes
        </div>
    </div>
);

const BallotContestResultUndervote = () => (
    <div className='pt-card'>
        <div className='pt-card'>
            <div>Ballot contest 3</div>
            <div>Governor</div>
        </div>
        <div className='pt-card'>
            Undervote
        </div>
        <div className='pt-card'>
            Comments: Faint markings visible.
        </div>
    </div>
);

const BallotContestReview = ({ comments, contest, marks, noConsensus }: any) => {
    const { votesAllowed } = contest;
    const votesMarked = marks.length;

    const noConsensusDiv = (
        <div>
            The Audit Board did not reach consensus.
        </div>
    );

    const noMarksDiv = (
        <div>
            Blank Vote
        </div>
    );

    const markedChoiceDivs = _.map(marks, (m: any) => {
        return (
            <div key={ m.id } className='pt-card'>
                <div>{ m.name }</div>
            </div>
        );
    });

    const markedChoices = () => {
        if (votesMarked > votesAllowed) {
            return (
                <div>
                    <strong>Overvote</strong> for this contest.
                </div>
            );
        }

        return (
            <div>
                <strong>Votes for:</strong>
                { markedChoiceDivs.length ? markedChoiceDivs : noMarksDiv }
            </div>
        );
    };

    return (
        <div className='pt-card'>
            <div className='pt-card'>
                <div>{ contest.name }</div>
                <div>{ contest.description }</div>
                <div>Vote for { contest.votesAllowed }</div>
            </div>
            <div className='pt-card'>
                { noConsensus ? noConsensusDiv : markedChoices() }
            </div>
            <div className='pt-card'>
                Comments: { comments }
            </div>
        </div>
    );
};

const BallotReview = ({ ballotMarks }: any) => {
    const contestReviews = _.map(ballotMarks, (contestMarks: any) => {
        const key = contestMarks.contest.id;
        return <BallotContestReview key={ key } { ...contestMarks } />;
    });

    return <div className='pt-card'>{ contestReviews }</div>;
};

const ReviewStage = (props: any) => {
    const {
        county,
        currentBallot,
        marks: rawMarks,
        nextStage,
        prevStage,
        selectNextBallot,
    } = props;

    const ballotMarks = _.mapValues(rawMarks, ({choices, comments, noConsensus }: any, contestId: any) => {
        const contest = findById(county.contests, contestId);
        const marks = _.map(choices, (id: any) => findById(contest.choices, id));

        return { comments, contest, marks, noConsensus };
    });

    const onClick  = () => {
        selectNextBallot();
        nextStage();
    };

    return (
        <div>
            <BallotReview ballotMarks={ ballotMarks } />
            <div className='pt-card'>
                <BackButton back={ prevStage } />
                <button className='pt-button pt-intent-primary' onClick={ onClick }>
                    Submit & Next Ballot
                </button>
            </div>
        </div>
    );
};


export default ReviewStage;
