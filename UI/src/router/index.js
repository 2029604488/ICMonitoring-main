import Vue from 'vue'
import VueRouter from 'vue-router'
import login from '../components/login'

Vue.use(VueRouter)

const routes = [
    {
        path: '/login',
        name: 'login',
        component: login
    },
    {
        path: '/',
        name: 'Main',
        redirect: '/heatData',
        component: () => import(/* webpackChunkName: "Main" */ '../components/Main'),
        children: [
             {
                path: '/heatData',
                name: "heatData",
                component: () => import(/* webpackChunkName: "Home" */ '../components/heatData')
            },
            {
                path: '/chart',
                name: "chart",
                component: () => import(/* webpackChunkName: "Home" */ '../components/chart')
            },{
                path: '/graph',
                name: "graph",
                component: () => import(/* webpackChunkName: "Home" */ '../components/graph')
            },{
                path: '/dynamic',
                name: "dynamic",
                component: () => import(/* webpackChunkName: "Home" */ '../components/DynamicGraph')
            },{
                path: '/user',
                name: "user",
                component: () => import(/* webpackChunkName: "Home" */ '../components/user')
            },{
                path: '/manage',
                name: "manage",
                component: () => import(/* webpackChunkName: "Home" */ '../components/manage')
            },
            {
                path: '/column',
                name: "column",
                component: () => import(/* webpackChunkName: "Home" */ '../components/column')
            },
            {
                path: '/forecast',
                name: "forecast",
                component: () => import(/* webpackChunkName: "Home" */ '../components/forecast')
            },
        ]
    },

]
const originalPush = VueRouter.prototype.push
VueRouter.prototype.push = function push(location) {
    return originalPush.call(this, location).catch(err => err)
}
const router = new VueRouter({
     mode:"hash",
    // base: process.env.BASE_RUL,
    routes
})
//??????
router.beforeEach((to, from, next) => {
    //to ??????
    //from ?????????
    //next ?????????????????????
    if (to.path === '/login') {
        return next();
    } else {
        const token = window.sessionStorage.getItem("token");
        if (!token) {
            return next('login');
        }
        next();
    }
})


export default router
