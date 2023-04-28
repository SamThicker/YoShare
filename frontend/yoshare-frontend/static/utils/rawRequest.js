import axios from "axios";
import store from "../../src/store";
import { MessageBox } from "element-ui";
import { getToken } from "../utils/auth";

// 创建axios实例
const service = axios.create({
  baseURL: "/raw" /*process.env.BASE_API,*/,
  timeout: 15000, //请求超时时间，单位毫秒
  withCredentials: true //加了这段就可以跨域了
});

// request拦截器
service.interceptors.request.use(
  config => {
    if (store.getters.token) {
      config.headers["Authentication"] = getToken(); //让每个请求携带自定义token 请根据实际情况自行修改
    }
    return config;
  },
  error => {
    // Do something with request error
    console.log(error); // for debug
    Promise.reject(error);
  }
);

// response拦截器
service.interceptors.response.use(
  response => {
    /**
     * code为非200是抛错 结合自己业务修改
     * */
    const res = response.data;
    if (res.code !== 200) {
      //401:未登录;
      if (res.code === 401) {
        MessageBox.confirm("请登录", "出错", {
          confirmButtonText: "重新登录",
          cancelButtonText: "取消",
          type: "warning"
        }).then(() => {
          store.dispatch("FedLogOut").then(() => {
            //重新实例化vue-router避免bug
            location.reload();
          });
        });
      }
      return Promise.reject(res);
    } else {
      return Promise.resolve(res);
    }
  },
  error => {
    console.log("err" + error);
    return Promise.reject(error);
  }
);

export default service;
