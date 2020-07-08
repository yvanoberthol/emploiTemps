(function() {
    'use strict';

    angular
        .module('myApp')
        .controller('TrimestreDialogController', TrimestreDialogController);

    TrimestreDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Trimestre'];

    function TrimestreDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Trimestre) {
        var vm = this;
        console.log("trimestre dialog")
        vm.trimestre = entity;
        console.log(vm.trimestre);

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
            if (vm.trimestre.id !== null) {
                Trimestre.update(vm.trimestre, onSaveSuccess, onSaveError);
            } else {
                Trimestre.save(vm.trimestre, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('myApp:trimestreUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }
    }
})();
