'use strict';

const path = require('path');
const pui_server = 'https://xxxxxx/pui9_server';

module.exports = {
	dev: {
		// Paths
		assetsSubDirectory: 'jspui9/static',
		jsFilesSubDirectory: 'jspui9',
		assetsPublicPath: '/',
		proxyTable: {
                '/pui9_dev_server/**': {
                        target: pui_server,
                        secure: false,
                        changeOrigin: true,
                        pathRewrite: { '^/pui9_dev_server': '' }
                },
                '/pui9core': {
                        target: 'http://localhost:9999',
                        pathRewrite: { '^/pui9core': '' }
                },
                '/static_pui9core': {
                        target: 'http://localhost:9999/static_pui9core',
                        pathRewrite: { '^/static_pui9core': '' }
                },
                '/assets_pui9core': {
                        target: 'http://localhost:9999/assets_pui9core',
                        pathRewrite: { '^/assets_pui9core': '' }
                },
                '/jspui9': {
                        target: 'http://localhost:9998/jspui9',
                        pathRewrite: { '^/jspui9': '' }
                },
                '/static': {
                        target: ' http://localhost:9998/static',
                        pathRewrite: { '^/static': '' }
                }
        },

		// Various Dev Server settings
		host: 'localhost', // can be overwritten by process.env.HOST
		port: 9096, // can be overwritten by process.env.PORT, if port is in use, a free one will be determined
		autoOpenBrowser: false,
		errorOverlay: true,
		notifyOnErrors: true,
		poll: false, // https://webpack.js.org/configuration/dev-server/#devserver-watchoptions-

		// Use Eslint Loader?
		// If true, your code will be linted during bundling and
		// linting errors and warnings will be shown in the console.
		useEslint: true,
		// If true, eslint errors and warnings will also be shown in the error overlay
		// in the browser.
		showEslintErrorsInOverlay: false,

		/**
		 * Source Maps
		 */

		// https://webpack.js.org/configuration/devtool/#development
		devtool: 'inline-source-map',

		// If you have problems debugging vue-files in devtools,
		// set this to false - it *may* help
		// https://vue-loader.vuejs.org/en/options.html#cachebusting
		cacheBusting: false,
		cssSourceMap: true
	},

	build: {
		// Template for index.html
		index: path.resolve(__dirname, '../dist/index.html'),

		// Paths
		assetsRoot: path.resolve(__dirname, '../dist'),
		assetsSubDirectory: 'static',
		jsFilesSubDirectory: '',
		assetsPublicPath: '/',

		/**
		 * Source Maps
		 */

		productionSourceMap: true,
		// https://webpack.js.org/configuration/devtool/#production
		devtool: '#source-map',

		// Gzip off by default as many popular static hosts such as
		// Surge or Netlify already gzip all static assets for you.
		// Before setting to `true`, make sure to:
		// npm install --save-dev compression-webpack-plugin
		productionGzip: false,
		productionGzipExtensions: ['js', 'css'],

		// Run the build command with an extra argument to
		// View the bundle analyzer report after build finishes:
		// `npm run build --report`
		// Set to `true` or `false` to always turn it on or off
		bundleAnalyzerReport: process.env.npm_config_report
	}
};
