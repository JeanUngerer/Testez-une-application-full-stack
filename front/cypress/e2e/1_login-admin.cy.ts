describe('Login spec', () => {
  it('should have submit button disabled while form is not filled', () => {
    cy.visit('/login')
    cy.get('[type="submit"]').should('be.disabled');
  })

  it('should show error message', () => {
    cy.visit('/login')
    cy.get('input[formControlName=email]').type("yogaga@studio.com")
    cy.get('input[formControlName=password]').type(`${"testfalse!1234"}{enter}{enter}`)
    cy.get('[type="submit"]').should('be.unabled');
    cy.get('[data-cy="error"]').should('exist')
  })


  it('should log in successfully', () => {
    cy.visit('/login')
    cy.get('input[formControlName=email]').type("yoga@studio.com")
    cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`)

    cy.url().should('include', '/sessions')
  })
});
