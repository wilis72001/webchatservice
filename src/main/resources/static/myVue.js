new Vue({
    el: '#app3',
    data() {
        return {
            userName: "vtest2@qq.com",
            apiResponseTime:'',
            username_from :'',
            username_to : '',
            username_end : 'vtest3@qq.com',
            numThreads : 0,
            apiResponseData : ''
        };
    },
    methods: {
        registerUser() {
            fetch('/register', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ userName: this.userName })
            })
                .then(response => response.text())
                .then(data => {
                    console.log(data); // 处理成功响应
                    this.apiResponseData = data;

                })
                .catch(error => {
                    console.error(error); // 处理错误响应
                });
        },
        batRegisterUser() {
            fetch('/batchRegister', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({
                    username_from: this.username_from,
                    username_to: this.username_to,
                    username_end: this.username_end,
                    numThreads : this.numThreads
                })
            })
                .then(response => response.text())
                .then(data => {
                    console.log(data); // 处理成功响应
                    this.apiResponseTime = data;

                })
                .catch(error => {
                    console.error(error); // 处理错误响应
                });
        }
    }
});

