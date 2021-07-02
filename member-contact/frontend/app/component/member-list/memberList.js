
angular.module('memberApp').component('memberList', {
  templateUrl: 'component/member-list/member-list-view.html',
  controller: memberListController
});

memberListController.$inject = ['MemberResource', 'NgTableParams'];
function memberListController(MemberResource, NgTableParams) {
  var vm = this;

  vm.$onInit = function() {
    MemberResource.getAll().$promise.then(function(response){
      vm.tableParams = new NgTableParams({}, {
        dataset: response,
        counts: []
      });
    });
  };
}