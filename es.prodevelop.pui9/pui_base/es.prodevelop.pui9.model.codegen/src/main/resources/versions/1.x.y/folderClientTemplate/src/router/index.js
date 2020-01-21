// your own components imports
import storeConfig from '@/store/store';

import pui9api from 'pui9';

// layout component from pui9, the only component that it is required to retrieve by using pui9api
// eslint-disable-next-line import/no-duplicates

const PuiBaseLayout = pui9api.getRegisteredComponentByName('puibaselayout');
const PuiUserGrid = pui9api.getRegisteredComponentByName('puiusergrid');
const PuiUserForm = pui9api.getRegisteredComponentByName('puiuserform');
const PuiUserSettingsForm = pui9api.getRegisteredComponentByName('puiusersettingsform');
const PuiProfileGrid = pui9api.getRegisteredComponentByName('puiprofilegrid');
const PuiProfileForm = pui9api.getRegisteredComponentByName('puiprofileform');
const PuiFunctionalityGrid = pui9api.getRegisteredComponentByName('puifunctionalitygrid');
const PuiVariableGrid = pui9api.getRegisteredComponentByName('puivariablegrid');
const PuiVariableForm = pui9api.getRegisteredComponentByName('puivariableform');
const PuiLanguageGrid = pui9api.getRegisteredComponentByName('puilanguagegrid');
const PuiLanguageForm = pui9api.getRegisteredComponentByName('puilanguageform');
const PuiLoginGrid = pui9api.getRegisteredComponentByName('puilogingrid');
const PuiSessionGrid = pui9api.getRegisteredComponentByName('puisessiongrid');
const PuiDocumentGrid = pui9api.getRegisteredComponentByName('puidocumentgrid');
const PuiDocumentForm = pui9api.getRegisteredComponentByName('puidocumentform');
const PuiWellcomePanel = pui9api.getRegisteredComponentByName('puiwellcomepanel');
const PuiDocgenTemplateGrid = pui9api.getRegisteredComponentByName('puidocgentemplategrid');
const PuiDocgenTemplateForm = pui9api.getRegisteredComponentByName('puidocgentemplateform');

// import here your application components
// import MyAppComponent from '@/components/MyAppComponent.vue';

// registry here your application components if it is needed
// pui9api.registryComponent(MyAppComponent.name, MyAppComponent);

const configRouter = {
	path: `/${storeConfig.state.global.appName}`,
	name: 'puibaselayout',
	component: PuiBaseLayout,
	children: [
		{
			path: 'home',
			component: PuiWellcomePanel
		},
		{
			path: 'login',
			component: PuiLoginGrid
		},
		{
			path: 'session',
			component: PuiSessionGrid
		},
		{
			path: 'puilanguage',
			component: PuiLanguageGrid
		},
		{
			path: '(.*/)?puilanguage/:method/:pk',
			props: true,
			component: PuiLanguageForm
		},
		{
			path: 'puivariable',
			component: PuiVariableGrid
		},
		{
			path: '(.*/)?puivariable/:method/:pk',
			props: true,
			component: PuiVariableForm
		},
		{
			path: 'puidocument',
			component: PuiDocumentGrid
		},
		{
			path: '(.*/)?puidocument/:method/:pk',
			props: true,
			component: PuiDocumentForm
		},
		{
			path: 'puidocgentemplate',
			component: PuiDocgenTemplateGrid
		},
		{
			path: '(.*)puidocgentemplate/:method/:pk',
			props: true,
			component: PuiDocgenTemplateForm
		},
		{
			path: 'puiuser',
			component: PuiUserGrid
		},
		{
			path: '(.*/)?puiuser/:method/:pk',
			props: true,
			component: PuiUserForm
		},
		{
			path: '(.*/)?usersettings',
			component: PuiUserSettingsForm
		},
		{
			path: 'puiprofile',
			component: PuiProfileGrid
		},
		{
			path: '(.*/)?puiprofile/:method/:pk',
			props: true,
			component: PuiProfileForm
		},
		{
			path: 'puifunctionality',
			component: PuiFunctionalityGrid
		}
	]
};

export default configRouter;
