
angular.module('memberApp').component('member', {
  templateUrl: 'component/member/member-view.html',
  controller: memberController
});


memberController.$inject = ['MemberResource', '$routeParams'];
function memberController(MemberResource, $routeParams) {
  var vm = this;

  vm.$onInit = function () {
    vm.member = MemberResource.getMember({id:$routeParams.id});
  }

}