const webpack = require('webpack');
const path = require('path');


module.exports = {
    entry: [
        './src/index.tsx',
    ],

    output: {
        filename: 'bundle.prod.js',

        path: path.join(__dirname, 'dist'),

        publicPath: '/static/',
    },

    resolve: {
        extensions: ['.ts', '.tsx', '.js', '.json']
    },

    plugins: [
        new webpack.NamedModulesPlugin(),

        new webpack.NoEmitOnErrorsPlugin(),
    ],

    module: {
        rules: [
            {
                test: /\.tsx?$/,
                loaders: [
                    'awesome-typescript-loader'
                ],
                exclude: path.join(__dirname, 'node_modules'),
                include: path.join(__dirname, 'src'),
            },
        ],
    },

    devtool: 'inline-source-map',
};
