<template>
    <div>
        <img class="toggle-img" :src="imgUrl" style="position: absolute; top: 0px; right: -100px"/>
        <p class="prompt-text" :style="styleObj">
            {{ promptText }}
        </p>
        
    </div>
</template>

<script>
export default {
    data() {
        return {
            promptText: "一切正常运转！",
            imgUrl: require("@/assets/happy1.png"),
            promptGreenStyle: {
                'background-color': '#00da00',
                'box-shadow': '0px 0px 10px #00da00',
                '--before-border-color': '#00da00'
            },
            promptRedStyle: {
                'background-color': '#ff2516',
                'box-shadow': '0px 0px 10px #ff2516',
                '--before-border-color': '#ff2516'
            },
            styleObj: null,
        };
    },
    methods: {     
         happy3() {
            this.imgUrl = require("@/assets/happy1.png");
            this.styleObj = this.promptGreenStyle;
            this.promptText = "频率1正常运转！";
        },
         happy4() {
            this.imgUrl = require("@/assets/happy1.png");
            this.styleObj = this.promptGreenStyle;
            this.promptText = "频率2正常运转！";
        },
          A3alarm1() {
            this.imgUrl = require("@/assets/award1.png");
            this.styleObj = this.promptRedStyle;
            this.promptText = "频率1有一级异常出现！";
        },
         A3alarm2() {
            this.imgUrl = require("@/assets/award2.png");
            this.styleObj = this.promptRedStyle;
            this.promptText = "频率1有二级异常出现！";
        },
         A3alarm3() {
            this.imgUrl = require("@/assets/award3.png");
            this.styleObj = this.promptRedStyle;
            this.promptText = "频率1有三级异常出现！";
        },
        A4alarm1() {
            this.imgUrl = require("@/assets/award1.png");
            this.styleObj = this.promptRedStyle;
            this.promptText = "频率2有一级异常出现！";
        },
         A4alarm2() {
            this.imgUrl = require("@/assets/award2.png");
            this.styleObj = this.promptRedStyle;
            this.promptText = "频率2有二级异常出现！";
        },
         A4alarm3() {
            this.imgUrl = require("@/assets/award3.png");
            this.styleObj = this.promptRedStyle;
            this.promptText = "频率2有三级异常出现！";
        },
    },
    mounted() {
        this.styleObj = this.promptGreenStyle;
    }
};
</script>

<style scoped>
.toggle-img {
    width: 80px;
    height: 80px;
    float: right;
    
}
.prompt-text {
    width: max-content;
    height: 40px;

    float: left;
    line-height: 40px;
    padding: 0 40px;
    margin: 0;
    margin-left: 20px;

    position: relative;

    font-weight: bold;

    color: white;
    border-radius: 10px;
    background-color: #00da00;
    box-shadow: 0px 0px 10px #21d521;
    transition: 1.5s all;

    z-index: 2;
}

.prompt-text::before {
    content: "";
    display: block;
    width: 0;
    height: 0;

    position: absolute;
    top: 29px;
    right: -14px;
    transform: rotateZ(-140deg);

    border-top: 10px solid transparent;
    border-right: 30px solid var(--before-border-color);
    border-bottom: 10px solid transparent;
    border-radius: 10px;
    transition: 1.5s all;
    z-index: 1;
}

</style>
