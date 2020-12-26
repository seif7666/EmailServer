import { createApp } from "vue";
import App from "./App.vue";
import router from "./router";

const BACKEND_URL = "http://localhost:5500/api/Database/";
import axios from "axios";
export { BACKEND_URL, axios };

createApp(App)
  .use(router)
  .mount("#app");
