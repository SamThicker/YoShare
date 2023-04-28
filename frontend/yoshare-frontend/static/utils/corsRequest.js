import axios from "axios";

// 创建axios实例
const service = axios.create({
  baseURL: "/res" /*process.env.BASE_API,*/,
  timeout: 15000, //请求超时时间，单位毫秒
  withCredentials: true //加了这段就可以跨域了
});

// request拦截器
service.interceptors.request.use(
  config => {
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
    const res = response;
    if (res.status !== 200) {
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
