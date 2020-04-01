import Vue from "vue";
import App from "./App.vue";
import router from "./router";
import store from "./store";
import "element-ui/lib/theme-chalk/index.css";
import { Message } from "element-ui";
import ElementUI from "element-ui";
import "vue-awesome/icons/flag";

Vue.use(ElementUI);
Vue.config.productionTip = false;

//地区选择
import VDistpicker from "v-distpicker";
Vue.component("v-distpicker", VDistpicker);

//文本编辑器
import Editor from "./components/Editor";
Vue.component("Editor", Editor);

//mavon
import MarkdownEditor from "./components/MarkdownEditor";
Vue.component("MarkdownEditor", MarkdownEditor);

//markdown-it-vue
import MarkdownItVue from 'markdown-it-vue';
import 'markdown-it-vue/dist/markdown-it-vue.css';
Vue.component("MarkdownItVue", MarkdownItVue);

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount("#app");

//elementUI消息框封装
Vue.prototype.$elementMessage = function(msg, t, d) {
  Message({
    message: msg,
    type: t,
    during: d,
    showClose: true,
    center: true
  });
};

//富文本编辑器
import VueQuillEditor from "vue-quill-editor";
// require styles 引入样式
import "quill/dist/quill.core.css";
import "quill/dist/quill.snow.css";
import "quill/dist/quill.bubble.css";
Vue.use(VueQuillEditor);
if (module.hot) {
  module.hot.accept();
}
