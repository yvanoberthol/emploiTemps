(function() {
    'use strict';

    angular
        .module('myApp')
        .controller('GroupController', GroupController);

    GroupController.$inject = ['$rootScope', '$scope', '$stateParams', 'session', '$state', '$http',
                                'Group', '$filter', 'NgTableParams'];

    function GroupController ($rootScope, $scope, $stateParams, session, $state, $http,
                                Group, $filter, NgTableParams) {
        var vm = this;
        vm.loading = true;

        var initialParams = {
            count: 10, // initial page size
            sorting: { 'name': "asc" }
        };

        vm.tableParams = new NgTableParams(initialParams, {
             // page size buttons (right set of buttons in demo)
             //counts: [10,25,50,100],
             counts: [],

             //filterDelay: 1000,
             // determines the pager buttons (left set of buttons in demo)
             paginationMaxBlocks: 10,
             paginationMinBlocks: 2,

             getData: function(params) {
             //vm.loading = true;

             if(params.sorting()){
                 //console.log(params.sorting());
                 for(var sortBy in params.sorting()){
                   vm.sortBy = sortBy;
                   vm.direction = params.sorting()[sortBy];
                 }
             }

             return Group.getAll( params.page()-1,        //page
                                    params.count(),         //size
                                    vm.sortBy,              //sortBy
                                    vm.direction)
             .then(function(response){
                   params.total(response.data.totalElements);
                   //vm.loading = false;
                   //return response.data.content;
                   vm.groups = response.data.content;
                   vm.loading = false;
                   return vm.groups;
             })
           }
        });

        vm.updateEnabled = function(group){
            group.enabled = !group.enabled;
            Group.update(group);
        }
    }
})();

