import Vue from 'vue'
import Router from 'vue-router'
import Home from './views/Home.vue'

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      name: 'home',
      component: Home
    },
    {
      path: '/test',
      name: 'test',
      component: () => import(/* webpackChunkName: "search" */ './views/test.vue')
    },
    {
      path: '/search',
      name: 'search',
      // route level code-splitting
      // this generates a separate chunk (about.[hash].js) for this route
      // which is lazy-loaded when the route is visited.
      redirect: '/search/choose',
      component: () => import(/* webpackChunkName: "search" */ './views/search.vue'),
      children: [
        {
          path: 'choose',
          name: 'choose',
          component: () => import(/* webpackChunkName: "choose" */ './views/choose.vue')
        },
        {
          path: 'picked',
          name: 'picked',
          component: () => import(/* webpackChunkName: "picked" */ './views/picked.vue')
        }
      ]
    },
    {
      path: '/backDesign',
      name: 'backDesign',
      component: () => import(/* webpackChunkName: "backDesign" */ './views/backDesign.vue')
    },
    {
      path: '/makeCover',
      name: 'makeCover',
      redirect: '/makeCover/firstHandle',
      component: () => import(/* webpackChunkName: "makeCover" */ './views/makeCover.vue'),
      children: [
        {
          path: 'firstHandle/:templateKid',
          name: 'firstHandle',
          props: true,
          component: () => import(/* webpackChunkName: "firstHandle" */ './views/firstHandle.vue')
        },
        {
          path: 'secondHandle/:templateKid',
          name: 'secondHandle',
          props: true,
          component: () => import(/* webpackChunkName: "secondHandle" */ './views/secondHandle.vue')
        }
      ]
    }
  ]
})
