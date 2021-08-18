yieldUnescaped '<!DOCTYPE html>'
html {
  head {
    title('Welcome to handbook')
  }
  body {
    div(class: 'container') {
      yield 'Welcome to handbook'
    }
  }
}