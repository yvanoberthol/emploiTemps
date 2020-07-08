(function() {
    'use strict';

    angular
        .module('myApp')
        .controller('InscriptionDetailController', InscriptionDetailController);

    InscriptionDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'SubInscription'];

    function InscriptionDetailController($scope, $rootScope, $stateParams, previousState, entity, SubInscription) {
        var vm = this;
        vm.inscription = entity;

        vm.previousState = previousState.name;


        vm.updateSubInscription = function(subInscription){
            subInscription.enabled = !subInscription.enabled;
            SubInscription.update(subInscription);
        }
    }
})();
