(function() {
    'use strict';

    angular
        .module('myApp')
        .controller('SmsTemplateDetailController', SmsTemplateDetailController);

    SmsTemplateDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'SubSmsTemplate'];

    function SmsTemplateDetailController($scope, $rootScope, $stateParams, previousState, entity, SubSmsTemplate) {
        var vm = this;
        vm.smsTemplate = entity;

        vm.previousState = previousState.name;


        vm.updateSubSmsTemplate = function(subSmsTemplate){
            subSmsTemplate.enabled = !subSmsTemplate.enabled;
            SubSmsTemplate.update(subSmsTemplate);
        }
    }
})();
