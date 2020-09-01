(function() {
    'use strict';

    angular
        .module('myApp')
        .controller('GroupDialogController', GroupDialogController);

    GroupDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Group'];

    function GroupDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Group) {
        var vm = this;
        vm.group = entity;
        console.log(vm.group);

        vm.clear = clear;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(0)>input').focus();
        });

        function clear () {
             $uibModalInstance.dismiss('cancel');
         }

        function save () {
            vm.isSaving = true;
            if (vm.group.id !== null) {
                Group.update(vm.group, onSaveSuccess, onSaveError);
            } else {
                Group.save(vm.group, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('myApp:groupUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }
    }
})();
