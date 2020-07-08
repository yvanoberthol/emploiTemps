(function() {
    'use strict';

    angular
        .module('myApp')
        .controller('SetupController', SetupController);

    SetupController.$inject = ['$scope', 'auth', '$stateParams', 'JhiLanguageService', '$translate', '$http',
                                   ];

    function SetupController ($scope, auth, $stateParams, JhiLanguageService, $translate, $http,
                                ) {
        var vm = this;
        vm.selected = 0;

        if($stateParams.view)
        vm.selected = $stateParams.view;

        vm.errorInfo = null;
        vm.successInfo = null;
        vm.save = save;
        vm.changePassword = changePassword;
        vm.doNotMatch = null;
        vm.errorPasswd = null;
        vm.successPasswd = null;

        var lat = 4.09191;
        var lng = 9.66975;

        $http.get("/api/account").then(function(response){
            vm.user = response.data;
        })

        function changePassword () {
            if (vm.password !== vm.confirmPassword) {
                vm.errorPasswd = null;
                vm.successPasswd = null;
                vm.doNotMatch = 'ERROR';
            } else {
                vm.doNotMatch = null;

                $http.post("/api/account/change-password", vm.password)
                .then(function(data){
                     vm.errorPasswd = null;
                     vm.successPasswd = 'OK';
                }, function() {
                      vm.successPasswd = null;
                      vm.errorPasswd = 'ERROR';
               });
            }
        }

        function save () {
            vm.errorInfo = null;
            vm.successInfo = null;
            $http.put("/api/users/", vm.user)
            .then(function(data){
                auth.refreshToken();
                  JhiLanguageService.getCurrent().then(function(current) {
                      if (vm.user.langKey !== current) {
                          $translate.use(vm.user.langKey);
                      }
                  });
                  vm.errorInfo = null;
                  vm.successInfo = true;
            }, function() {
                vm.errorInfo = true;
                vm.successInfo = null;
            });
        };
    }
})();
