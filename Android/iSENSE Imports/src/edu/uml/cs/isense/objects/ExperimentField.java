package edu.uml.cs.isense.objects;

/**
 * Class that includes information about a particular experiment field on iSENSE.
 * 
 * @author iSENSE Android Development Team
 */
public class ExperimentField {
	public int field_id;
	public String field_name = "";
	public int type_id;
	public int unit_id;
	public String type_name = "";
	public String unit_name = "";
	public String unit_abbreviation = "";
	public String experiment_id = "";
	
	// type_id as-is in the database
	public final int TEMPERATURE = 1;
	public final int LENGTH = 2;
	public final int DISTANCE = 3;
	public final int FORCE = 4;
	public final int VOLUME = 5;
	public final int MASS = 6;
	public final int TIME = 7;
	public final int LUMINOUS_FLUX = 8;
	public final int LUMINOUS_INTENSITY = 9;
	public final int ANGLE = 10;
	public final int ELECTRIC_POTENTIAL = 11;
	public final int ELECTRIC_CURRENT = 12;
	public final int POWER = 13;
	public final int ELECTRIC_CHARGE = 14;
	public final int SPEED = 15;
	public final int BOOLEAN = 16;
	public final int PERCENTAGE = 17;
	public final int ANALOG = 18;
	public final int GEOSPACIAL = 19;
	public final int RATE = 20;
	public final int NUMERIC = 21;
	public final int CUSTOM = 22;
	public final int SALINITY = 23;
	public final int PH_LEVEL = 24;
	public final int ACCELERATION = 25;
	public final int UV = 26;
	public final int PRESSURE = 27;
	public final int HUMIDITY = 28;
	public final int LIGHT = 29;
	public final int DISSOLVED_OXYGEN = 30;
	public final int ANEMOMETER = 31;
	public final int TURBIDITY = 32;
	public final int FLOW_RATE = 33;
	public final int MOTOR_MONITOR = 34;
	public final int CONDUCTIVITY = 35;
	public final int CONCENTRATION = 36;
	public final int TEXT = 37;
		
	public final int UNIT_LATITUDE = 57; 
	public final int UNIT_LONGITUDE = 58;
}

/*
type_id as-is in the database

+---------+--------------------+
| type_id | name               |
+---------+--------------------+
|       1 | Temperature        |
|       2 | Length             |
|       3 | Distance           |
|       4 | Force              |
|       5 | Volume             |
|       6 | Mass               |
|       7 | Time               |
|       8 | Luminous Flux      |
|       9 | Luminous Intensity |
|      10 | Angle              |
|      11 | Electric Potential |
|      12 | Electric Current   |
|      13 | Power              |
|      14 | Electric Charge    |
|      15 | Speed              |
|      16 | Boolean            |
|      17 | Percentage         |
|      18 | Analog             |
|      19 | Geospacial         |
|      20 | Rate               |
|      21 | Numeric            |
|      22 | Custom             |
|      23 | Salinity           |
|      24 | pH Level           |
|      25 | Acceleration       |
|      26 | UV                 |
|      27 | Pressure           |
|      28 | Humidity           |
|      29 | Light              |
|      30 | Dissolved Oxygen   |
|      31 | Anemometer         |
|      32 | Turbidity          |
|      33 | Flow Rate          |
|      34 | Motor Monitor      |
|      35 | Conductivity       |
|      36 | Concentration      |
|      37 | Text               |
+---------+--------------------+

+---------+-------------------------------+--------------+
| unit_id | name                          | abbreviation |
+---------+-------------------------------+--------------+
|       1 | Kelvin                        | K            |
|       2 | Celsius                       | C            |
|       3 | Fahrenheit                    | F            |
|       4 | Kilometer                     | km           |
|       5 | Meter                         | m            |
|       6 | Centimeter                    | cm           |
|       7 | Millimeter                    | mm           |
|       8 | Nanometer                     | nm           |
|       9 | Mile                          | mi           |
|      10 | Yard                          | yd           |
|      11 | Foot                          | ft           |
|      12 | Inch                          | in           |
|      13 | Pound                         | lb           |
|      14 | Newton                        | N            |
|      15 | Liter                         | L            |
|      16 | Milliliter                    | mL           |
|      17 | Gallon                        | gal          |
|      18 | Quart                         | qt           |
|      19 | Kilogram                      | kg           |
|      20 | Gram                          | g            |
|      21 | Milligram                     | mg           |
|      22 | UNIX Time                     | ms           |
|      23 | Julian Year                   | a            |
|      24 | Day                           | d            |
|      25 | Second                        | s            |
|      26 | Hour                          | h            |
|      27 | Minute                        | min          |
|      28 | Millisecond                   | ms           |
|      29 | Nanosecond                    | ns           |
|      30 | Lumen                         | lm           |
|      31 | Candela                       | cd           |
|      32 | Degree                        | deg          |
|      33 | Radian                        | rad          |
|      34 | Volt                          | V            |
|      35 | Ampere                        | A            |
|      36 | Milliamp                      | mA           |
|      37 | Watt                          | W            |
|      38 | Milliwatt                     | mW           |
|      39 | Kilowatt                      | kW           |
|      40 | Megawatt                      | MW           |
|      41 | Gigawatt                      | GW           |
|      42 | Coulomb                       | C            |
|      43 | Microcoulomb                  | uC           |
|      44 | Nanocoulomb                   | nC           |
|      45 | Meters per Second             | m/s          |
|      46 | Kilometers per Hour           | kph          |
|      47 | Feet per Second               | ft/s         |
|      48 | Miles per Hour                | mph          |
|      49 | PicoBoard Light               | %            |
|      50 | PicoBoard Sound               | %            |
|      51 | PicoBoard Button              | on/off       |
|      52 | SunSPOT Light                 | %            |
|      53 | Toggle Switch                 | on/off       |
|      54 | PicoBoard Slider              | %            |
|      55 | SunSPOT Button                | on/off       |
|      56 | Analog                        |              |
|      57 | Latitude                      | lat          |
|      58 | Longitude                     | lng          |
|      59 | Per Second                    | per sec      |
|      60 | Per Minute                    | per min      |
|      61 | Per Hour                      | per hour     |
|      62 | Per Day                       | per day      |
|      63 | Per Week                      | per week     |
|      64 | Per Month                     | per mont     |
|      65 | Per Year                      | per year     |
|      66 | Number                        | number       |
|      67 | Custom                        | Custom       |
|      68 | Lux                           | Lx           |
|      69 | Salinity                      | ppm          |
|      70 | pH Level                      | pH           |
|      71 | Meters Per Second Squared     | m/s^2        |
|      72 | UVA                           | mWs / cm     |
|      73 | UVB                           | mWs / cm     |
|      74 | Atmospheres                   | atms         |
|      75 | Pascals                       | Pa           |
|      76 | Relative Humidity             | rh           |
|      77 | Parts Per Million             | ppm          |
|      78 | Nephelometric Turbidity Units | NTU          |
|      79 | Siemens                       | uS           |
|      80 | Parts Per Million             | ppm          |
|      81 | Text                          |              |
+---------+-------------------------------+--------------+

*/