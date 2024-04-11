describe('empty spec', () => {

  it('should have valid user info', () => {
    cy.visit('/login')
    cy.get('input[formControlName=email]').type("yoga@studio.com")
    cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`)

    cy.url().should('include', '/sessions')


    cy.visit('/me')
    cy.get('[data-cy="name"]').should('eq', 'Admin ADMIN');
    cy.get('[data-cy="mail"]').should('eq', 'yoga@studio.com');
    cy.get('[data-cy="name"]').should('have.text', 'Admin ADMIN');
    cy.get('[data-cy="mail"]').should('have.text', 'yoga@studio.com');
  })
})
