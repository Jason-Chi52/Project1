import React from 'react'
import { Navbar, Nav, Container, Button } from 'react-bootstrap'
import { Link, NavLink, useNavigate } from 'react-router-dom'

export default function AppNavbar({ isAuthenticated, onLogout }) {
  const navigate = useNavigate()

  return (
    <Navbar bg="light" expand="lg" collapseOnSelect>
      <Container>
        <Navbar.Brand as={Link} to="/">CatalogApp</Navbar.Brand>
        <Navbar.Toggle aria-controls="main-nav" />
        <Navbar.Collapse id="main-nav">
          <Nav className="me-auto">
            <Nav.Link as={NavLink} to="/">Home</Nav.Link>
            {isAuthenticated && (
              <Nav.Link as={NavLink} to="/products">Products</Nav.Link>
            )}
          </Nav>
          <Nav>
            {isAuthenticated ? (
              <Button
                variant="outline-danger"
                onClick={() => { onLogout(); navigate('/') }}
              >
                Logout
              </Button>
            ) : (
              <>
                <Button
                  variant="outline-primary"
                  as={NavLink}
                  to="/login"
                  className="me-2"
                >
                  Login
                </Button>
                <Button
                  variant="primary"
                  as={NavLink}
                  to="/signup"
                >
                  Sign Up
                </Button>
              </>
            )}
          </Nav>
        </Navbar.Collapse>
      </Container>
    </Navbar>
  )
}
