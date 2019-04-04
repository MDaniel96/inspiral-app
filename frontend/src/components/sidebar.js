import React from "react";

class Sidebar extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      sidebarOpen: false
    };
  }
  render() {
    const siteNav = [
      {
        title: "Kezdőlap",
        url: "#top"
      },
      {
        title: "Bemutatkozás",
        url: "#about"
      },
      {
        title: "Tréningek",
        url: "#trainings"
      },
      {
        title: "Blog",
        url: "#blog"
      },
      {
        title: "Kapcsolat",
        url: "#contact"
      }
    ];
    return [
      <a
        key={"nav-menu-button"}
        className="menu-toggle rounded"
        onClick={() => this.onMenuClick()}
      >
        <i
          className={`fas ${this.state.sidebarOpen ? "fa-times" : "fa-bars"}`}
        />
      </a>,
      <nav
        key={"nav-menu"}
        id="sidebar-wrapper"
        className={this.state.sidebarOpen ? "active" : ""}
      >
        <ul className="sidebar-nav">
          <li className="sidebar-brand">
            <a className="js-scroll-trigger" href="#top">
              InSpiral Coaching
            </a>
          </li>
          {siteNav.map(link => (
            <li
              key={link.url}
              className="sidebar-nav-item"
              onClick={() => this.onMenuItemClick()}
            >
              <a className="js-scroll-trigger" href={link.url}>
                {link.title}
              </a>
            </li>
          ))}
        </ul>
      </nav>   
    ];
  }
  onMenuItemClick() {
    this.setState({ sidebarOpen: false });
  }
  onMenuClick() {
    this.setState({ sidebarOpen: !this.state.sidebarOpen });
    //      $(".menu-toggle > .fa-bars, .menu-toggle > .fa-times").toggleClass("fa-bars fa-times");
    //    $(this).toggleClass("active");
  }
}
export default Sidebar;
