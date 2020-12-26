import { createRouter, createWebHistory } from "vue-router";
import Home from "../views/Home.vue";

const routes = [
  {
    path: "/",
    name: "Home",
    component: Home
  },
  {
    path: "/about",
    name: "About",
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () =>
      import(/* webpackChunkName: "about" */ "../views/About.vue")
  },
  {
    path: "/login",
    name: "login",
    component: () =>
        import(/* webpackChunkName: "about" */ "../views/login.vue")
  },
  {
    path: "/signup",
    name: "signup",
    component: () =>
        import(/* webpackChunkName: "about" */ "../views/signup.vue")
  },
  {
    path: "/mailbox",
    name: "mailbox",
    component: () =>
        import(/* webpackChunkName: "about" */ "../views/mailbox.vue")
  },
  {
    path: "/singlemail",
    name: "singlemail",
    component: () =>
        import(/* webpackChunkName: "about" */ "../views/singlemail.vue")
  },
  {
    path: "/compose",
    name: "compose",
    component: () =>
        import(/* webpackChunkName: "about" */ "../views/compose.vue")
  },
  {
    path: "/contacts",
    name: "contacts",
    component: () => import("../views/contacts.vue")
  },
  {
    path: "/addcontact",
    name: "addcontact",
    component: () => import("../views/addcontact.vue")
  },
  {
    path: "/singlecontact",
    name: "singlecontact",
    component: () => import("../views/singlecontact.vue")
  }
];

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
});

export default router;
