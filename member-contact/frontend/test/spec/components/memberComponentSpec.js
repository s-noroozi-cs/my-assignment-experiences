'use strict';

describe('Component: Member', function () {

  var component, memberResource, routeParams, $compile, $scope;
  var mockMemberResponse = {
    name: "test-name",
    contacts: [{
      name: 'test-contact-name',
      email: 'test-contact-email',
      phone: 'test-contact-phone'
    }]
  };

  beforeEach(function () {
    module('memberApp');
    module('templates');
    inject(function (_$componentController_, _MemberResource_, _$routeParams_, _$compile_, _$rootScope_) {
      $scope = _$rootScope_.$new();
      $compile = _$compile_;
      memberResource = _MemberResource_;
      routeParams = _$routeParams_;
      component = _$componentController_('member', {
        MemberResource: memberResource,
        $routeParams: routeParams
      });
    });
  });

  it('should make request to get member on load', function () {
    var testMemberShipId = 123;
    routeParams.id = testMemberShipId;
    spyOn(memberResource, "getMember").and.returnValue(mockMemberResponse);
    component.$onInit();
    expect(memberResource.getMember).toHaveBeenCalledWith({id:testMemberShipId});
  });

  it('should render component', function() {
    var testMemberShipId = 123;
    routeParams.id = testMemberShipId;
    spyOn(memberResource, "getMember").and.returnValue(mockMemberResponse);
    var element = $compile(angular.element("<member></member>"))($scope);
    $scope.$digest();

    expect(element.find('.head').text()).toBe('Details for test-name');
    expect(element.find('td')[0].innerHTML).toBe(mockMemberResponse.contacts[0].name);
    expect(element.find('td')[1].innerHTML).toBe(mockMemberResponse.contacts[0].phone);
    expect(element.find('td')[2].innerHTML).toBe(mockMemberResponse.contacts[0].email);
  });
});