.. java:import:: java.awt.event ActionEvent

.. java:import:: java.awt.event ItemEvent

.. java:import:: java.awt.event ItemListener

.. java:import:: java.lang.reflect Constructor

.. java:import:: javax.swing AbstractAction

.. java:import:: javax.swing JButton

.. java:import:: javax.swing JComboBox

.. java:import:: javax.swing JLabel

.. java:import:: javax.swing JTextField

.. java:import:: ca.nengo.config ClassRegistry

.. java:import:: ca.nengo.math.impl IndicatorPDF

.. java:import:: ca.nengo.model.impl NodeFactory

.. java:import:: ca.nengo.model.neuron.impl ALIFNeuronFactory

.. java:import:: ca.nengo.model.neuron.impl LIFNeuronFactory

.. java:import:: ca.nengo.model.neuron.impl PoissonSpikeGenerator

.. java:import:: ca.nengo.model.neuron.impl PoissonSpikeGenerator.LinearNeuronFactory

.. java:import:: ca.nengo.model.neuron.impl PoissonSpikeGenerator.SigmoidNeuronFactory

.. java:import:: ca.nengo.model.neuron.impl SpikeGeneratorFactory

.. java:import:: ca.nengo.model.neuron.impl SpikingNeuronFactory

.. java:import:: ca.nengo.model.neuron.impl SynapticIntegratorFactory

.. java:import:: ca.nengo.ui.configurable ConfigException

.. java:import:: ca.nengo.ui.configurable ConfigResult

.. java:import:: ca.nengo.ui.configurable ConfigSchema

.. java:import:: ca.nengo.ui.configurable ConfigSchemaImpl

.. java:import:: ca.nengo.ui.configurable Property

.. java:import:: ca.nengo.ui.configurable PropertyInputPanel

.. java:import:: ca.nengo.ui.configurable.descriptors PBoolean

.. java:import:: ca.nengo.ui.configurable.descriptors PFloat

.. java:import:: ca.nengo.ui.lib.util UserMessages

.. java:import:: ca.nengo.ui.models.constructors AbstractConstructable

.. java:import:: ca.nengo.ui.models.constructors ModelFactory

CLinearNeuronFactory
====================

.. java:package:: ca.nengo.ui.configurable.panels
   :noindex:

.. java:type::  class CLinearNeuronFactory extends ConstructableNodeFactory

   Constructable Linear Neuron Factory

   :author: Shu Wu

Fields
------
pIntercept
^^^^^^^^^^

.. java:field:: static final Property pIntercept
   :outertype: CLinearNeuronFactory

pMaxRate
^^^^^^^^

.. java:field:: static final Property pMaxRate
   :outertype: CLinearNeuronFactory

pRectified
^^^^^^^^^^

.. java:field:: static final Property pRectified
   :outertype: CLinearNeuronFactory

zConfig
^^^^^^^

.. java:field:: static final ConfigSchema zConfig
   :outertype: CLinearNeuronFactory

Constructors
------------
CLinearNeuronFactory
^^^^^^^^^^^^^^^^^^^^

.. java:constructor:: public CLinearNeuronFactory()
   :outertype: CLinearNeuronFactory

Methods
-------
createNodeFactory
^^^^^^^^^^^^^^^^^

.. java:method:: @Override protected NodeFactory createNodeFactory(ConfigResult configuredProperties)
   :outertype: CLinearNeuronFactory

getSchema
^^^^^^^^^

.. java:method:: @Override public ConfigSchema getSchema()
   :outertype: CLinearNeuronFactory

