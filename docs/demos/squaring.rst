Squaring an input
=================

**Purpose**:
This demo shows how to construct a network
that squares the value encoded in one population
through the connection to a second population.

**Comments**:
This is a simple nonlinear function
being decoded in the connection weights between the cells.
Previous demos are linear decodings.

**Usage**:
Grab the slider control and move it up and down
to see the effects of increasing or decreasing input.
Notice that the output value
does not go negative even for negative inputs.
Dragging the input slowly from -1 to 1
will approximately trace a quadratic curve in the output.

.. image:: images/squaring.png
   :width: 100%

.. literalinclude:: ../../simulator-ui/dist-files/demo/squaring.py

.. topic:: Video:

   .. raw:: html

      <iframe width="100%" height="400" src="https://www.youtube.com/embed/rje217cSxkY" frameborder="0" allowfullscreen></iframe>
