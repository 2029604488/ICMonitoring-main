<template>
  <div>
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
    </a-form-model>
    <a-row :gutter="[16,16]" style="margin-top: 20px">
      <a-col :span="12">
        <graph id="tu1" ref="g1"></graph>
      </a-col>
      <a-col :span="12">
        <graph  id="tu2" ref="g2"></graph>
      </a-col>
    </a-row>
    <a-row :gutter="[16,16]">
      <a-col :span="12">
        <graph  id="tu3" ref="g3"></graph>
      </a-col>
      <a-col :span="12">
        <graph  id="tu4" ref="g4"></graph>
      </a-col>
    </a-row>
  </div>
</template>

<script>
import Graph from "@/components/graph";
import moment from "moment";
import locale from "ant-design-vue/lib/date-picker/locale/zh_CN";
import SockJS from "sockjs-client";
import Stomp from "stompjs";

export default {
  name: "DynamicGraph",
  components: {Graph},
  data() {
    return {
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
      stompClient: '',
      chart: null,
    }
  },
  mounted() {
    this.initWebSocket();
    this.initData();
    const that = this;
    this.$http.get("getStationsRunning").then(function (res) {
      that.stations = res.data.result;
    }).catch()
  },
  methods: {
    handleSubmit() {
      this.$message.success("修改成功！");
      //把数据置成空！
      this.$refs.g1.option.series[0].data = []
      this.$refs.g1.option.series[1].data = []
      this.$refs.g1.option.series[2].data = []
      this.$refs.g1.option.series[3].data = []
      this.$refs.g2.option.series[0].data = []
      this.$refs.g2.option.series[1].data = []
      this.$refs.g3.option.series[0].data = []
      this.$refs.g3.option.series[1].data = []
      this.$refs.g3.option.series[2].data = []
      this.$refs.g3.option.series[3].data = []
      this.$refs.g4.option.series[0].data = []
      this.$refs.g4.option.series[1].data = []
      this.$refs.g4.option.series[2].data = []
    },
    initData() {
      this.$refs.g2.option.legend.data = ['芯片损耗', '内存空间']
      this.$refs.g3.option.legend.data = ['芯片电源电压', '芯片工作电压', '芯片输入电压', '芯片输出电压']
      this.$refs.g4.option.legend.data = ['芯片频率1', '芯片频率2', '芯片频率3']
      this.$refs.g2.option.title.text = '历史芯片温度'
      this.$refs.g3.option.title.text = '芯片运行速度'
      this.$refs.g4.option.title.text = '芯片历史状态'
      this.$refs.g2.option.series = [
        {
          name: '芯片损耗',
          type: 'line',
          data: []
        },
        {
          name: '内存空间',
          type: 'line',
          data: []
        },]
      this.$refs.g3.option.series = [
        {
          name: '芯片电源电压',
          type: 'line',
          data: []
        },
        {
          name: '芯片工作电压',
          type: 'line',
          data: []
        }, {
          name: '芯片输入电压',
          type: 'line',
          data: []
        }, {
          name: '芯片输出电压',
          type: 'line',
          data: []
        },
      ]
      this.$refs.g4.option.series = [
        {
          name: '芯片频率1',
          type: 'line',
          data: []
        },
        {
          name: '芯片频率2',
          type: 'line',
          data: []
        }, {
          name: '芯片频率3',
          type: 'line',
          data: []
        },
      ]
    },
    initWebSocket() {
      this.connection();
      // let that = this;
      // 断开重连机制,尝试发送消息,捕获异常发生时重连
      // this.timer = setInterval(() => {
      //   try {
      //     that.stompClient.send("test");
      //   } catch (err) {
      //     console.log("断线了: " + err);
      //     that.connection();
      //   }
      // }, 5000);
    },
    connection: function () {
      // 建立连接对象
      const that = this;
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
          console.log(msg.body);
          const message = JSON.parse(msg.body);
          that.changeValue(message);
        }, headers);
        // this.stompClient.send("/app/test", headers, JSON.stringify({sender: 'clider', chatType: 'JOIN'}))
      }, (err) => {
        // 连接发生错误时的处理函数
        console.log('失败')
        console.log(err);
      });
    },    //连接 后台
    // 断开连接
    disconnect() {
      if (this.stompClient) {
        this.stompClient.disconnect();
      }
    },
    changeValue(res) {
      const that = this;
      if (res.heatExchangeStation.toString() !== that.formDate.station) {
        return;
      }
      const size = this.formDate.gap * 60 / 2;
      if (this.$refs.g1.option.xAxis.data.length === size) {
        that.$refs.g1.option.xAxis.data.shift();
        that.$refs.g2.option.xAxis.data.shift();
        that.$refs.g3.option.xAxis.data.shift();
        that.$refs.g4.option.xAxis.data.shift();
        that.$refs.g1.option.series[0].data.shift();
        that.$refs.g1.option.series[1].data.shift();
        that.$refs.g1.option.series[2].data.shift();
        that.$refs.g1.option.series[3].data.shift();
        that.$refs.g2.option.series[0].data.shift();
        that.$refs.g2.option.series[1].data.shift();
        that.$refs.g3.option.series[0].data.shift();
        that.$refs.g3.option.series[1].data.shift();
        that.$refs.g3.option.series[2].data.shift();
        that.$refs.g3.option.series[3].data.shift();
        that.$refs.g4.option.series[0].data.shift();
        that.$refs.g4.option.series[1].data.shift();
        that.$refs.g4.option.series[2].data.shift();
      }
      const time = moment(res.time).format("YYYY-MM-DD HH:mm:ss");
      that.$refs.g1.option.xAxis.data.push(time);
      that.$refs.g2.option.xAxis.data.push(time);
      that.$refs.g3.option.xAxis.data.push(time);
      that.$refs.g4.option.xAxis.data.push(time);
      that.$refs.g1.option.series[0].data.push(res.oneSupplyTemperature);
      that.$refs.g1.option.series[1].data.push(res.onceTemperature);
      that.$refs.g1.option.series[2].data.push(res.twoSupplyTemperature);
      that.$refs.g1.option.series[3].data.push(res.twiceTemperature);
      that.$refs.g2.option.series[0].data.push(res.instantaneousTraffic);
      that.$refs.g2.option.series[1].data.push(res.instantaneousHeat);
      that.$refs.g3.option.series[0].data.push(res.oneSupplyPressure);
      that.$refs.g3.option.series[1].data.push(res.pressureOnce);
      that.$refs.g3.option.series[2].data.push(res.twoSupplyPressure);
      that.$refs.g3.option.series[3].data.push(res.pressureTwice);
      that.$refs.g4.option.series[0].data.push(res.electricValveOpening);
      that.$refs.g4.option.series[1].data.push(res.circulatingPumpFrequency);
      that.$refs.g4.option.series[2].data.push(res.makeupPumpFrequency);
    }
  }
}
</script>

<style scoped>
#tu1{
  background-image: url("~@/assets/bg.jpg");
}
#tu2{
  background-image: url("~@/assets/bg.jpg");
}
#tu3{
  background-image: url("~@/assets/bg.jpg");
}
#tu4{
  background-image: url("~@/assets/bg.jpg");
}
</style>
