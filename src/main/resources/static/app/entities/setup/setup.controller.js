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

        if($stateParams.view)
        vm.selected = $stateParams.view;

    }
})();
