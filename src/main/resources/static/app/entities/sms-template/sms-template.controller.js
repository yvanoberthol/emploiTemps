(function() {
    'use strict';

    angular
        .module('myApp')
        .controller('SmsTemplateController', SmsTemplateController);

    SmsTemplateController.$inject = ['$rootScope', '$scope', '$stateParams', 'session', '$state',
                                'SmsTemplate', '$filter', 'NgTableParams'];

    function SmsTemplateController ($rootScope, $scope, $stateParams, session, $state,
                                SmsTemplate, $filter, NgTableParams) {
        var vm = this;
        vm.loading = true;

        var initialParams = {
            count: 10, // initial page size
            sorting: { 'title': "asc" }
        };

        SmsTemplate.getAll()
        .then(function(response){
            vm.loading = false;
            vm.smsTemplates = response.data;
            vm.tableParams = new NgTableParams(initialParams, {
                counts: [],
                // page size buttons (right set of buttons in demo)
                //counts: [10,25,50,100],
                //filterDelay: 1000,
                // determines the pager buttons (left set of buttons in demo)
                paginationMaxBlocks: 10,
                paginationMinBlocks: 2,
                dataset: vm.smsTemplates
            });
        });
    }
})();

