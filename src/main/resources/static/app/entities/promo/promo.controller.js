(function() {
    'use strict';

    angular
        .module('myApp')
        .controller('PromoController', PromoController);

    PromoController.$inject = ['$rootScope', '$scope', '$stateParams', 'session', '$state', '$http',
                                'Promo', '$filter', 'NgTableParams'];

    function PromoController ($rootScope, $scope, $stateParams, session, $state, $http,
                                Promo, $filter, NgTableParams) {
        var vm = this;
        vm.loading = true;

        var initialParamsPromos = {
            count: 10, // initial page size
            sorting: { 'name': "asc" }
        };

        Promo.getByAnnee(session.getAnneeId())
            .then(function(response){
                vm.loading = false;
                vm.promos = response.data;
                console.log(vm.promos);
                vm.tableParams = new NgTableParams(initialParamsPromos, {
                    counts: [],
                    // page size buttons (right set of buttons in demo)
                    //counts: [10,25,50,100],
                    //filterDelay: 1000,
                    // determines the pager buttons (left set of buttons in demo)
                    paginationMaxBlocks: 10,
                    paginationMinBlocks: 2,
                    dataset: vm.promos
                });
        })

        vm.updateEnabled = function(promo){
            promo.enabled = !promo.enabled;
            Promo.update(promo);
        }
    }
})();

