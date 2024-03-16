import { createRouter, createWebHistory } from "vue-router";
import HomeView from "../views/HomeView.vue";

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: "/",
      name: "home",
      component: HomeView,
    },
    {
      path: "/about",
      name: "about",
      // route level code-splitting
      // this generates a separate chunk (About.[hash].js) for this route
      // which is lazy-loaded when the route is visited.
      component: () => import("../views/MainPageView.vue"),
    },
    {
      path: "/login",
      name: "login",
      component: () => import("../views/LoginPageView.vue"),
    },
    {
      path: "/group",
      name: "group",
      component: () => import("../views/GroupSelectView.vue"),
    },
    {
      path: "/admin",
      name: "admin",
      component: () => import("../views/AdminPageView.vue"),
    },
    {
      path: "/admin/password",
      name: "adminPassword",
      component: () => import("../views/AdminPasswordView.vue"),
    },
    {
      path: "/newGroupForm",
      name: "newGroupForm",
      component: () => import("../views/NewGroupFormView.vue"),
    },
  ],
});

export default router;
