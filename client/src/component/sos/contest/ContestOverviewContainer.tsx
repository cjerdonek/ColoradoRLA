import * as React from 'react';
import { connect } from 'react-redux';

import ContestOverviewPage from './ContestOverviewPage';


class ContestOverviewContainer extends React.Component<any, any> {
    public render() {
        return <ContestOverviewPage { ...this.props } />;
    }
}

const mapStateToProps = ({ contests }: any) => ({ contests });

const mapDispatchToProps = (dispatch: any) => ({});

export default connect(
    mapStateToProps,
    mapDispatchToProps,
)(ContestOverviewContainer);
