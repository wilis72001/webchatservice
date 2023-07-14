new Vue({
    el: '#app1',
    data: {
        accountName : '',

    },
    methods: {
        oneKeyRegister(){
            fetch('http://192.168.19.101:8080/oneKeyRegister', {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json'
                }
            })
                .then(response => response.text())
                .then(data => {
                    console.log(data);
                    this.accountName = data;
                })
                .catch(error => {
                    console.error('Error:', error);
                });
        },
            }


})