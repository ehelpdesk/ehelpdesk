const webpack = require('webpack');
const CopyWebpackPlugin = require('copy-webpack-plugin');
const HtmlWebpackPlugin = require('html-webpack-plugin');
const rxPaths = require('rxjs/_esm5/path-mapping');

const utils = require('./utils.js');

module.exports = (options) => ({
    resolve: {
        extensions: ['.ts', '.js'],
        modules: ['node_modules'],
        alias: {
            app: utils.root('src/main/development/app/'),
            ...rxPaths()
        }
    },
    stats: {
        children: false
    },
    module: {
        rules: [
            {
                test: /\.html$/,
                loader: 'html-loader',
                options: {
                    minimize: true,
                    caseSensitive: true,
                    removeAttributeQuotes:false,
                    minifyJS:false,
                    minifyCSS:false
                },
                exclude: ['/src/main/development/index.html']
            },
            {
                test: /\.(jpe?g|png|gif|svg|woff2?|ttf|eot)$/i,
                loader: 'file-loader',
                options: {
                    digest: 'hex',
                    hash: 'sha512',
                    name: 'content/[hash].[ext]'
                }
            },
            {
                test: /manifest.webapp$/,
                loader: 'file-loader',
                options: {
                    name: 'manifest.webapp'
                }
            },
            // Ignore warnings about System.import in Angular
            { test: /[\/\\]@angular[\/\\].+\.js$/, parser: { system: true } },
        ]
    },
    plugins: [
        new webpack.DefinePlugin({
            'process.env': {
                NODE_ENV: `'${options.env}'`,
                BUILD_TIMESTAMP: `'${new Date().getTime()}'`,
                VERSION: `'${utils.parseVersion()}'`,
                DEBUG_INFO_ENABLED: options.env === 'development',
                // The root URL for API calls, ending with a '/' - for example: `"https://login.isscorporatesolutions.com/myservice/"`.
                // If this URL is left empty (""), then it will be relative to the current context.
                SERVER_API_URL: `''`
            }
        }),
        new CopyWebpackPlugin([
            { from: './src/main/development/content/', to: 'content' },
            { from: './src/main/development/ehelpdesk.png', to: 'ehelpdesk.png' },
            { from: './src/main/development/manifest.webapp', to: 'manifest.webapp' },
            { from: './src/main/development/robots.txt', to: 'robots.txt' }
        ]),
        new HtmlWebpackPlugin({
            template: './src/main/development/index.html',
            chunks: ['vendors', 'polyfills', 'global', 'main'],
            chunksSortMode: 'manual',
            inject: 'body'
        })
    ]
});
