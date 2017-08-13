import { Dispatch } from 'redux';

import { apiHost } from '../config';


const createFetchAction = ({
    failType,
    networkFailType,
    okType,
    sendType,
    url,
}: any) => () => {
    return (dispatch: Dispatch<any>) => {
        dispatch({ type: sendType });

        fetch(url)
            .then(r => {
                if (!r.ok) {
                    dispatch({ type: failType });
                    return;
                }

                r.json().then((data: any) => {
                    dispatch({ type: okType, data });
                });
            })
            .catch(() => {
                dispatch({ type: networkFailType });
            });
    };
};


export default createFetchAction;