///PRODUCTION PUI9
//PUI9 IMPORTS
import 'pui9/dist/pui9.css';
import 'pui9/dist/manifest';
import 'pui9/dist/pui9-externals';
import pui9api from 'pui9';

//application specific store config (state, getters, mutations, actions) and routing
import storeConfig from './store/store';
import theRouting from './router';

//CSS
import './styles/app.css';

//languages
import messagesEn from '../static/translations/en.json';
import messagesEs from '../static/translations/es.json';
import messagesCa from '../static/translations/ca.json';
import messagesFr from '../static/translations/fr.json';

pui9api.setBaseUrl(process.env.PUISERVER);

//mandatory pui9 steps for include app own configurations of store(state,getters,setters), routes and translations. Then runApp()
pui9api.resetStoreConfig(storeConfig);
pui9api.addRoutes([theRouting]);

pui9api.addTranslations('en', messagesEn);
pui9api.addTranslations('es', messagesEs);
pui9api.addTranslations('ca', messagesCa);
pui9api.addTranslations('fr', messagesFr);

pui9api.runApp();
