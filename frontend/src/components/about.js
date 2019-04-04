import React from "react";
import ScrollableAnchor from "react-scrollable-anchor";

const AboutSection = () => (
  <ScrollableAnchor id="about">
    <section className="content-section bg-light">
      <div className="container text-center">
        <div className="row">
          <div className="col-lg-5 mx-auto">
            <h2>
              Itt lesz a bemutatkozás
            </h2>
            <p className="lead mb-5">
              itt is 
            </p>
            <a
              className="btn btn-dark btn-xl js-scroll-trigger"
              href="#blog"
            >
              Olvass a blogomról
            </a>
          </div>
        </div>
</div> 



    </section>
  </ScrollableAnchor>
);

export default AboutSection;
