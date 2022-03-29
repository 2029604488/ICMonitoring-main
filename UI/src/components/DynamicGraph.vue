<template>
  <div id="boarders">
    <smile ref="smile" style="position: absolute; top: 0px; left: 50px"></smile>
    <a-form-model layout="inline" :model="formDate" @submit="handleSubmit" @submit.native.prevent>
      <a-form-model-item label="芯片：">
        <a-select default-value="飞腾系列1" v-model="formDate.station">
          <a-select-option v-for="i in stations" :key="i" :value="i">
            {{ i }}
          </a-select-option>
        </a-select>
      </a-form-model-item>
      <a-form-model-item label="测试时间间隔">
        <a-select default-value="飞腾系列1" v-model="formDate.gap">
          <a-select-option v-for="i in gaps" :key="i" :value="i">
            {{ i }}
          </a-select-option>
        </a-select>
      </a-form-model-item>
      <a-form-model-item>
        <a-button
            type="primary"
            html-type="submit"
            :disabled="formDate.station === ''">
          确定
        </a-button>
      </a-form-model-item>
      <a-form-model-item>
        响应时间: {{ loadTime }} ms
      </a-form-model-item>
    </a-form-model>
    <div class="container">
      <div class="item a" @click="clickChart(0)" style="transform: translate(95%,-33%) scale(0.33)">
        <pieChart ref="g1"></pieChart>
      </div>
      <div class="item b" @click="clickChart(1)" style="transform: translate(95%,35%) scale(0.33)">
        <radarChart ref="g2"></radarChart>
      </div>
      <div class="item c" @click="clickChart(2)" style="transform: translate(95%,1%) scale(0.33)">
        <lineChart ref="g3"></lineChart>
      </div>
      <div class="item d active" @click="clickChart(3)" style="transform: translate(20%, 0) scale(1)">
        <barChart ref="g4"></barChart>
      </div>
<!--      <div class="item e" @click="clickChart(4)" style="transform: translate(98%, 20%) scale(0.33)">-->
     <!--   <warning ref="g5"></warning>-->
<!--      </div>-->
    </div>
  </div>
</template>

<script>
import moment from "moment";
import SockJS from 'sockjs-client';
import Stomp from 'stompjs';
import barChart from "./charts/barChart";
import pieChart from "./charts/pieChart";
import radarChart from "./charts/radarChart";
import lineChart from "./charts/lineChart";

import smile from "./charts/dyemotion";
import locale from "ant-design-vue/lib/date-picker/locale/zh_CN";

export default {
  name: "column",
  components: {
    barChart,
    pieChart,
    radarChart,
    lineChart,
    smile,
   
  },

  data() {
    return {
      updateCount: 0,
      item: [],
      gaps: [2, 4, 5, 10, 20],
      nowStation: '',
      formDate: {
        gap: 2,
        station: "飞腾系列1", start: moment().subtract(1, 'hour').format("YYYY-MM-DD HH:mm:ss"),
        end: moment().format("YYYY-MM-DD HH:mm:ss")
      },
      stations: [],
      moment,
      locale,
      loadTime: 0,
      startLoadTime: 0,
      endLoadTime: 0,
      receiveCount: 0,
      stompClient: '',
      chart: null,
    }
  },
  mounted() {
    this.begin();
    this.initWebSocket();
    this.initData();
    const that = this;
    this.$http.get("getStationsRunning").then(function (res) {
      that.stations = res.data.result;
    }).catch()
  },
  methods: {
    begin() {
      this.items = document.querySelectorAll('.container .item')
      for (let i = 0; i < this.items.length; i++) {
        this.items[i].order = i;
        const start = new Date()
        this.startLoadTime = start.getTime()
      }
    },
    clickChart(clickIndex) {
      let activeItem = document.querySelector('.container .active')
      let activeIndex = activeItem.order
      let clickItem = this.items[clickIndex]
      if (activeIndex === clickIndex) {
        return
      }
      activeItem.classList.remove('active')
      clickItem.classList.add('active')
      this._transform(clickItem, activeItem)
    },
    _transform(el1, el2) {
      let transform1 = el1.style.transform
      let transform2 = el2.style.transform
      el1.style.transform = transform2
      el2.style.transform = transform1
    },
    duringTime(time) {
      const showTime = new Date();
      const occurTime = new Date(time);
      return showTime.getTime() - occurTime.getTime() + '毫秒'
    },
    handleSubmit() {
      const start = new Date()
      this.startLoadTime = start.getTime()
      this.loadTime = 0;
      this.$message.success("修改成功！");
      //把数据置成空！
      
      this.$refs.g1.option.series[0].data[0].value = []
      this.$refs.g1.option.series[0].data[1].value = []
      this.$refs.g1.option.series[0].data[2].value = []
      this.$refs.g1.option.series[0].data[3].value = []
      this.$refs.g2.option.series[0].data[0].value[0] = []
      this.$refs.g2.option.series[0].data[0].value[1] = []
      this.$refs.g2.option.series[0].data[0].value[2] = []
      this.$refs.g2.option.series[0].data[0].value[3] = []
      this.$refs.g2.option.series[0].data[0].value[4] = []
      this.$refs.g2.option.series[0].data[0].value[5] = []
      this.$refs.g2.option.series[0].data[1].value[0] = []
      this.$refs.g2.option.series[0].data[1].value[1] = []
      this.$refs.g2.option.series[0].data[1].value[2] = []
      this.$refs.g2.option.series[0].data[1].value[3] = []
      this.$refs.g2.option.series[0].data[1].value[4] = []
      this.$refs.g2.option.series[0].data[1].value[5] = []
      this.$refs.g3.option.series[0].data = []
      this.$refs.g3.option.series[1].data = []
      this.$refs.g3.option.series[2].data = []
      this.$refs.g3.option.series[3].data = []
      this.$refs.g3.option.series[4].data = []
      this.$refs.g4.option.series[0].data = []
      this.$refs.g4.option.series[1].data = []
      this.$refs.g4.option.series[2].data = []
      this.$refs.g4.option.series[3].data = []
      this.receiveCount = 0
    },
    initData() {
      this.$refs.g2.option.legend.data = ['芯片损耗', '芯片空间使用']
      this.$refs.g3.option.legend.data = ['芯片电源电压', '芯片工作电压', '芯片输入电压', '芯片输出电压', '芯片电流']
      this.$refs.g4.option.legend.data = ['芯片频率1', '芯片频率2', '芯片频率3', '芯片频率4']
      this.$refs.g2.option.title.text = '芯片温度'
      this.$refs.g3.option.title.text = '芯片运行速度'
      this.$refs.g4.option.title.text = '芯片状态'
      this.$refs.g1.option.series = [
        {
          name: '芯片温度',
          type: 'pie',
          data: [{value: {}, name: '芯片1:温度'},
            {value: {}, name: '芯片2:温度'},
            {value: {}, name: '芯片3:温度'},
            {value: {}, name: '芯片4:温度'}]
        }
      ]

      this.$refs.g3.option.series = [
        {
          name: '芯片电源电压',
          type: 'line',
          smooth: false,
          data: []
        },
        {
          name: '芯片工作电压',
          type: 'line',
          smooth: false,
          data: []
        }, {
          name: '芯片输入电压',
          type: 'line',
          smooth: false,
          data: []
        }, {
          name: '芯片输出电压',
          type: 'line',
          smooth: false,
          data: []
        },
        {
          name: '芯片电流',
          type: 'line',
          smooth: false,
          data: []
        },
      ]
      this.$refs.g4.option.series = [
        {
          name: '芯片频率1',
          type: 'bar',
          data: [],
          barWidth: 30,
        },
        {
          name: '芯片频率2',
          type: 'bar',
          data: [],
          barWidth: 30,
        }, {
          name: '芯片频率3',
          type: 'bar',
          data: [],
          barWidth: 30,
        },
        {
          name: '芯片频率4',
          type: 'bar',
          data: [],
          barWidth: 30,
        },
      ]
    },
    initWebSocket() {
      this.connection();
      let that = this;
      // 断开重连机制,尝试发送消息,捕获异常发生时重连
      this.timer = setInterval(() => {
        try {
          that.stompClient.send("秦少乾11");
        } catch (err) {
          console.log("断线了: " + err);
          that.connection();
        }
      }, 5000);
    },
    connection() {
      const that = this;
      // 建立连接对象
      let socket = new SockJS('http://127.0.0.1:8887/ws');
      // 获取STOMP子协议的客户端对象
      this.stompClient = Stomp.over(socket);
      // 定义客户端的认证信息,按需求配置
      let headers = {
        Authorization: ''
      }
      // 向服务器发起websocket连接
      this.stompClient.connect(headers, () => {
        this.stompClient.subscribe('/topic/public', (msg) => { // 订阅服务端提供的某个topic
          const message = JSON.parse(msg.body);
          that.changeValue(message);
          if (this.receiveCount === 0) {
            const end = new Date()
            this.endLoadTime = end.getTime()
            this.loadTime = this.endLoadTime - this.startLoadTime
            this.receiveCount = 1
          }
        }, headers);
        this.stompClient.send("成功了",
            headers,
            JSON.stringify({sender: '', chatType: 'JOIN'}),
        )
        //用户加入接口
      }, (err) => {
        // 连接发生错误时的处理函数
        console.log('失败')
        console.log(err);
      });
    },    //连接 后台
    disconnect() {
      if (this.stompClient) {
        this.stompClient.disconnect();
      }
    },  // 断开连接
    changeValue(res) {
      console.log(res);
      const that = this;
      if (res.heatExchangeStation.toString() !== that.formDate.station) {
        return;
      }
      const size = this.formDate.gap * 60 / 2;
      if (this.$refs.g3.option.xAxis.data.length === 10 * size) {
        that.$refs.g3.option.xAxis.data.shift();
        that.$refs.g4.option.xAxis.data.shift();
        that.$refs.g3.option.series[0].data.shift();
        that.$refs.g3.option.series[1].data.shift();
        that.$refs.g3.option.series[2].data.shift();
        that.$refs.g3.option.series[3].data.shift();
        that.$refs.g3.option.series[4].data.shift();
        that.$refs.g4.option.series[0].data.shift();
        that.$refs.g4.option.series[1].data.shift();
        that.$refs.g4.option.series[2].data.shift();
        that.$refs.g4.option.series[3].data.shift();
      }
      this.$refs.g2.option.series = [
        {
          name: '芯片损耗和空间使用',
          type: 'radar',
          data: [
            {
              value: new Array(6),
              name: '芯片损耗'
            },
            {
              value: new Array(6),
              name: '芯片空间使用'
            }
          ]
        }
      ]
      const time = moment(res.time).format("YYYY-MM-DD HH:mm:ss");
      //表1 饼图
      that.$refs.g1.option.series[0].data[0].value = res.onceTemperature;
      that.$refs.g1.option.series[0].data[1].value = res.oneSupplyTemperature;
      that.$refs.g1.option.series[0].data[2].value = res.twiceTemperature;
      that.$refs.g1.option.series[0].data[3].value = res.twoSupplyTemperature;
      // 表2 雷达图
      this.$refs.g2.option.series[0].data[0].value[0] = res.oneSupplyTemperature;
      this.$refs.g2.option.series[0].data[0].value[1] = res.onceTemperature;
      this.$refs.g2.option.series[0].data[0].value[2] = res.twoSupplyTemperature;
      this.$refs.g2.option.series[0].data[0].value[3] = res.twiceTemperature;
      this.$refs.g2.option.series[0].data[0].value[4] = res.pressureOnce;
      this.$refs.g2.option.series[0].data[0].value[5] = res.oneSupplyPressure;

      this.$refs.g2.option.series[0].data[1].value[0] = res.oneSupplyTemperature - 4;
      this.$refs.g2.option.series[0].data[1].value[1] = res.onceTemperature + 8;
      this.$refs.g2.option.series[0].data[1].value[2] = res.twoSupplyTemperature + 8;
      this.$refs.g2.option.series[0].data[1].value[3] = res.twiceTemperature + 8;
      this.$refs.g2.option.series[0].data[1].value[4] = res.pressureOnce - 0.1;
      this.$refs.g2.option.series[0].data[1].value[5] = res.oneSupplyPressure - 0.1;
      //表3 折线图
      that.$refs.g3.option.xAxis.data.push(time);
      that.$refs.g3.option.series[0].data.push(res.oneSupplyTemperature);
      that.$refs.g3.option.series[1].data.push(res.onceTemperature);
      that.$refs.g3.option.series[2].data.push(res.twoSupplyTemperature);
      that.$refs.g3.option.series[3].data.push(res.twiceTemperature);
      that.$refs.g3.option.series[4].data.push(res.instantaneousTraffic);
      //表4 柱状图
      that.$refs.g4.option.xAxis.data.push(time);
      that.$refs.g4.option.series[0].data.push(res.oneSupplyPressure);
      that.$refs.g4.option.series[1].data.push(res.pressureOnce);
      that.$refs.g4.option.series[2].data.push(res.twoSupplyPressure);
      that.$refs.g4.option.series[3].data.push(res.pressureTwice);
      that.updateCount++;
      
      if (res.oneTemperature > 60) {
         this.$refs.smile.awkward();
        this.onceAni = false;
      }
      if (res.instantaneousTraffic > 83) {
         this.$refs.smile.awkward();
        this.onceAni = false;
      }
      if (res.oneSupplyPressure > 0.85) {
         this.$refs.smile.awkward();
        this.onceAni = false;
      }
      if (res.pressureOnce > 0.7) {
          this.$refs.smile.awkward();
        this.onceAni = false;
      }
      
      
    },

    beforeDestroy: function () {
      // 页面离开时断开连接,清除定时器
      this.disconnect();
      clearInterval(this.timer);
    }
  },

}

</script>

<style>
#boarders {

  width: 100%;
  height: 900px;
  position: relative;

}

.container {
  position: relative;
  height: 90%;
  width: 95%;
  overflow: hidden;
  margin: 20px auto 0;
  box-sizing: content-box;
}

.item {
  padding: 0;
  margin: 0;
  width: 60%;
  height: 98%;
  position: absolute;
  text-align: center;
  background-color: #fff;
  /*background: rgba(248, 248, 252, 0.5);*/
  border-left: solid 1px #d2d0d0;
  border-top: solid 1px #d2d0d0;
  box-shadow: 5px 5px 5px #888;
  transition: all 0.8s;
}

.nav {
  margin: 0 auto;
  width: 80%;
}

.link {
  float: right;
  text-decoration: none;
  font-size: 20px;
  color: white;
}

.link:hover {
  color: aqua;
}
</style>
