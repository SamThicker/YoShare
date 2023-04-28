import Vue from "vue";
import VueRouter from "vue-router";
import Home from "../views/Home.vue";
import store from "../store";
// import App from "../App.vue";

Vue.use(VueRouter);

const routes = [
  //根目录
  {
    path: "/",
    name: "Home",
    component: Home,
    children: [
      {
        path: "group",
        name: "Group",
        meta: {
          requireAuth: true
        },
        component: () => import("@/components/Group.vue")
      },
      {
        path: "follow",
        name: "Follow",
        meta: {
          requireAuth: true
        },
        component: () => import("@/components/Follow.vue")
      },
      {
        path: "notice",
        name: "Notice",
        meta: {
          requireAuth: true
        },
        component: () => import("@/components/Notice.vue")
      },
      {
        path: "search",
        name: "Search",
        component: () => import("@/components/Search.vue")
      }
    ]
  },
  {
    path: "/about",
    name: "About",
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () =>
      import(/* webpackChunkName: "about" */ "." + "." + "/views/About.vue")
  },
  {
    path: "/login",
    name: "Login",
    component: () => import("../components/Login.vue")
  },
  {
    path: "/loginPage",
    name: "LoginPage",
    component: () => import("../views/LoginPage.vue")
  },
  {
    path: "/HeaderBar",
    name: "HeaderBar",
    component: () => import("../components/HeaderBar.vue")
  },
  {
    path: "/test",
    name: "Test",
    meta: {
      requireAuth: true
    },
    component: () => import("@/views/Test.vue")
  },
  {
    path: "/userHome",
    name: "UserHome",
    meta: {
      requireAuth: true
    },
    component: () => import("@/views/UserHome.vue")
  },
  //用户信息
  {
    path: "/userInfo",
    name: "UserInfo",
    meta: {
      requireAuth: true
    },
    component: () => import("@/views/UserInfo.vue"),
    children: [
      {
        path: "simpleInfo",
        component: () => import("@/components/SimpleInfo"),
        meta: {
          requireAuth: true
        }
      },
      {
        path: "securityInfo",
        component: () => import("@/components/SecurityInfo"),
        meta: {
          requireAuth: true
        }
      },
      {
        path: "studyRecord",
        component: () => import("@/components/memberResource/StudyRecord"),
        meta: {
          requireAuth: true
        }
      }
    ]
  },
  //用户笔记编辑
  {
    path: "/workBench/:userId/:noteId?",
    name: "WorkBench",
    meta: {
      requireAuth: true
    },
    component: () => import("@/views/WorkBench.vue")
  },
  {
    path: "/file",
    name: "File",
    meta: {
      requireAuth: true
    },
    component: () => import("@/views/File.vue")
  },
  //小组管理
  {
    path: "/group/:groupId",
    name: "GroupManage",
    meta: {
      requireAuth: true
    },
    component: () => import("@/components/GroupManage.vue"),
    children: [
      {
        path: "note",
        name: "GroupNote",
        component: () => import("@/components/group/GroupNote.vue"),
        children: [
          {
            path: ":noteId",
            name: "GroupNoteReading",
            component: () => import("@/components/group/GroupNoteReading.vue")
          }
        ]
      },
      {
        path: "file",
        name: "GroupFile",
        component: () => import("@/components/group/GroupFile.vue")
      },
      {
        path: "favorite",
        name: "GroupFavorite",
        component: () => import("@/components/group/GroupFavorite.vue")
      },
      {
        path: "member",
        name: "GroupMember",
        component: () => import("@/components/group/GroupMember.vue")
      },
      {
        path: "setting",
        name: "GroupSetting",
        component: () => import("@/components/group/GroupSetting.vue"),
        children: [
          {
            path: "simple",
            name: "GroupSimpleInfo",
            component: () => import("@/components/group/GroupSimpleInfo.vue")
          }
        ]
      }
    ]
  },
  //用户个人资源管理
  {
    path: "/resource",
    name: "Resource",
    children: [
      {
        path: "note",
        name: "ResourceNote",
        component: () =>
          import("@/components/memberResource/MemberResourceNote.vue"),
        children: [
          {
            path: "/resource/note/:noteId",
            name: "NoteReading",
            component: () => import("@/components/NoteReading.vue")
          }
        ]
      },
      {
        path: "file",
        name: "ResourceFile",
        component: () =>
          import("@/components/memberResource/MemberResourceFile.vue")
      },
      {
        path: "favorite",
        name: "ResourceFavorite",
        component: () =>
          import("@/components/memberResource/MemberReasourceFavorite.vue")
      }
    ],
    meta: {
      requireAuth: true
    },
    component: () => import("@/views/Resource.vue")
  }
];

const router = new VueRouter({
  routes
});

// 路由判断登录，根据路由配置文件的参数
router.beforeEach((to, from, next) => {
  if (to === from) return;
  //判断该路由是否需要登录权限
  if (to.matched.some(record => record.meta.requireAuth)) {
    //判断当前的token是否存在；登录存入的token
    // while (!store.getters.username);
    if (store.getters.token) {
      // if (localStorage.token) {
      next();
    } else {
      next({
        path: "/loginPage",
        query: { redirect: to.fullPath } //将跳转的路由path作为参数，登录成功后跳转到该路由
      });
    }
  } else {
    next();
  }
});

export default router;
