import * as React from 'react';

import { EditableText } from '@blueprintjs/core';


class CVRUploader extends React.Component<any, any> {
    public state: any = {
        file: null,
        hash: '',
    };

    public render() {
        const { forms, upload } = this.props;
        const { file, hash } = this.state;

        forms.cvrExportForm = this.state;

        const fileName = file ? file.name : '';

        return (
            <div className='pt-card'>
                Cast Vote Records:
                <div>
                    <label className='pt-file-upload'>
                        <input type='file' onChange={ this.onFileChange } />
                        <span className='pt-file-upload-input'>{ fileName }</span>
                    </label>
                </div>
                <EditableText
                    className='pt-input'
                    value={ hash }
                    onChange={ this.onHashChange } />
                <button className='pt-button' onClick={ upload }>
                    Upload
                </button>
            </div>
        );
    }

    public onFileChange = (e: any) => {
        const s = { ...this.state };

        s.file = e.target.files[0];

        this.setState(s);
    }

    public onHashChange = (hash: any) => {
        const s = { ...this.state };

        s.hash = hash;

        this.setState(s);
    }
}


export default CVRUploader;
