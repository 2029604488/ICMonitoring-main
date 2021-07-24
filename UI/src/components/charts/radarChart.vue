<template>
  <div class="b" style="height: 100%; height: 100%">
    <div id="mainB" style="width: 100%; height: 100%"></div>
  </div>
</template>

<script>
import echarts from 'echarts'
require('echarts/theme/macarons')
export default {
  name: "radarChart",
   props: {
    className: {
      type: String,
      default: 'chart'
    },
    width: {
      type: String,
      default: '100%'
    },
    height: {
      type: String,
      default: '300px'
    }
  },
  data(){
    return{
      option:{
      
        stompClient: '',
        ChartLineGraph:null,
        title: {
          text: '基础雷达图'
        },
         toolbox: {
               feature: {
               saveAsImage: {}
              }
            },
        legend: {
          data: ['芯片损耗', '内存空间']
        },
       radar: {
          // shape: 'circle',
        indicator: [
            { name: '温度1', max: 100},
            { name: '温度2', max: 100},
            { name: '电压1', max: 100},
            { name: '电压2', max: 100},
            { name: '频率1', max: 1},
            { name: '频率2', max: 1}
          ]
        },
        series: [{
          name: '环境影响',
          type: 'radar',
          data: [
            {
              value: [],
              name: '环境1'
            },
            {
              value: [],
              name: '环境2'
            }
          ]
        }]
      }
    }
  },
  methods:{
    setChart(){
     this.chart = echarts.init(this.$el, 'macarons')
      this.chart.setOption(
          this.option
      )
    }
  },
  mounted() {
        this.$nextTick(() => {
       this.setChart()
    })
  },
   beforeDestroy: function () {
    // 页面离开时断开连接,清除定时器
    if (!this.chart) {
      return
    }
    this.chart.dispose()
    this.chart = null
    this.disconnect();
  },
    //  深度监听 父组件刚开始没有值，只有图标的配置项
    //  父组件ajax请求后改变数据的值，传递过来，图标已生成，监听传过来的值的改变
// deep:true.深度监听，确保data中子项修改也能监听到。写法参考：https://cn.vuejs.org/v2/api/#watch
      watch: {
    option: {
      handler() {
        this.chart.setOption(this.option);
        this.chart.resize()
      }, deep: true
    }
  },
}

</script>

<style scoped>

</style>
