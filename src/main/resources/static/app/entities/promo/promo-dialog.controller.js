(function() {
    'use strict';

    angular
        .module('myApp')
        .controller('PromoDialogController', PromoDialogController);

    PromoDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Promo'];

    function PromoDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Promo) {
        var vm = this;
        vm.promo = entity;
        console.log(vm.promo);

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
            if (vm.promo.id !== null) {
                Promo.update(vm.promo, onSaveSuccess, onSaveError);
            } else {
                Promo.save(vm.promo, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('myApp:promoUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.promoActiveChanged = function(){
            if(!vm.promo.active)
                vm.promo.showOnStoreFront = false;
        }
    }
})();
