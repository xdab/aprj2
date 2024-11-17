grammar Packet;

packet:
	source GT destination (COMMA path)? COLON info? (
		LINEBREAK
		| EOF
	);

source: callsign;
destination: callsign;
path: callsign (COMMA callsign)*;
info: positionReport | status | otherInfo;

callsign: callsignBase (MINUS ssid)? repeated?;
callsignBase: alphanum alphanum alphanum alphanum*;
ssid: digit | (ONE from0to5);
repeated: ASTERISK;

timestamp: timestampDHM | timestampHMS | timestampMDHM;
timestampDHM: from00to31 from00to23 from00to59 timestampDHMZone;
timestampDHMZone: SLASH | 'z';
timestampHMS: from00to23 from00to59 from00to59 timestampHMSZone;
timestampHMSZone: 'h';
timestampMDHM: from01to12 from00to31 from00to23 from00to59;

positionReport: (positionReportIndicator positionReportBody)
	| (
		positionReportWithTimestampIndicator timestamp positionReportBody
	);
positionReportIndicator: EXCLAM | EQ;
positionReportWithTimestampIndicator: AT | SLASH;
positionReportBody: (positionUncompressed | positionCompressed) positionComment?;

positionUncompressed: latitude symbolTable longitude symbolCode;
latitude: latitudeDegrees latitudeDecimalMinutes northSouthIndicator;
latitudeDegrees: from00to89;
latitudeDecimalMinutes: fullMinutes DOT fractionalMinutes;
longitude: longitudeDegrees longitureDecimalMinutes westEastIndicator;
longitudeDegrees: from000to179;
longitureDecimalMinutes: fullMinutes DOT fractionalMinutes;
fullMinutes: from00to59;
fractionalMinutes: from00to99;

positionCompressed: symbolTable symbolCode any+; // TODO

symbolTable: any; // TODO
symbolCode: any; // TODO
northSouthIndicator: UPPERCASE_N | UPPERCASE_S;
westEastIndicator: UPPERCASE_W | UPPERCASE_E;

positionComment: any+; // TODO

status: GT any+;

otherInfo: any+;

// Utility rules

from01to12: (ZERO from1to9) | (ONE (ZERO | ONE | TWO));
from00to23: ((ZERO | ONE) digit)
	| (TWO (ZERO | ONE | TWO | THREE));
from00to31: ((ZERO | ONE | TWO) digit) | (THREE (ZERO | ONE));
from00to59: from0to5 digit;
from00to89: from0to8 digit;
from00to99: digit digit;
from000to179: (ZERO digit digit) | (ONE from0to7 digit);
from0to5: ZERO | ONE | TWO | THREE | FOUR | FIVE;
from0to7: ZERO | ONE | TWO | THREE | FOUR | FIVE | SIX | SEVEN;
from0to8: ZERO | ONE | TWO | THREE | FOUR | FIVE | SIX | SEVEN | EIGHT;
from1to9:
	ONE
	| TWO
	| THREE
	| FOUR
	| FIVE
	| SIX
	| SEVEN
	| EIGHT
	| NINE;

alphanum: letter | digit;
any:
	letter
	| digit
	| GT
	| COLON
	| MINUS
	| ASTERISK
	| COMMA
	| EXCLAM
	| AT
	| SLASH
	| EQ
	| DOT;

digit:
	ZERO
	| ONE
	| TWO
	| THREE
	| FOUR
	| FIVE
	| SIX
	| SEVEN
	| EIGHT
	| NINE;

letter: lowercase | uppercase;

lowercase: LOWERCASE_H | LOWERCASE_Z | LOWERCASE_COMPLEMENT;
LOWERCASE_H: 'h';
LOWERCASE_Z: 'z';
LOWERCASE_COMPLEMENT: [a-gi-y];

uppercase: UPPERCASE_N | UPPERCASE_S | UPPERCASE_W | UPPERCASE_E | UPPERCASE_COMPLEMENT;
UPPERCASE_N: 'N';
UPPERCASE_S: 'S';
UPPERCASE_W: 'W';
UPPERCASE_E: 'E';
UPPERCASE_COMPLEMENT: [A-DF-MO-RT-VX-Z];

ZERO: '0';
ONE: '1';
TWO: '2';
THREE: '3';
FOUR: '4';
FIVE: '5';
SIX: '6';
SEVEN: '7';
EIGHT: '8';
NINE: '9';
GT: '>';
COLON: ':';
MINUS: '-';
ASTERISK: '*';
COMMA: ',';
EXCLAM: '!';
AT: '@';
SLASH: '/';
EQ: '=';
DOT: '.';
LINEBREAK: [\r\n];

// // Info is a sequence of characters INFO: POSITION_REPORT | STATUS_REPORT | MESSAGE | OBJECT |
// ITEM | CAPABILITIES | QUERY | TELEMETRY | WEATHER | WEATHER_POSITIONLESS | MIC_E | USER_DEFINED |
// THIRD_PARTY;

// // ==================== DATA TYPE INDICATORS ====================

// // Position report data type indicators IND_POS: IND_POS_NO_MESSAGING | IND_POS_MESSAGING;
// IND_POS_TIMESTAMP: IND_POS_TIMESTAMP_NO_MESSAGING | IND_POS_TIMESTAMP_MESSAGING;
// IND_POS_NO_MESSAGING: '!'; IND_POS_MESSAGING: '='; IND_POS_TIMESTAMP_NO_MESSAGING: '/';
// IND_POS_TIMESTAMP_MESSAGING: '@';

// // Status data type indicator IND_STATUS: '>';

// // Message data type indicator IND_MESSAGE: ':';

// // Object data type indicator IND_OBJECT: ';';

// // Item data type indicator IND_ITEM: ')';

// // Station capabilities data type indicator IND_CAPABILITIES: '<';

// // Query data type indicator IND_QUERY: '?';

// // Telemetry data type indicator IND_TELEMETRY: 'T';

// // Weather data type indicators IND_WEATHER: '!' | '#' | '$' | '*'; IND_WEATHER_POSITIONLESS:
// '_';

// // Mic-E data type indicators IND_MIC_E: IND_MIC_E_NEW | IND_MIC_E_OLD; IND_MIC_E_NEW: '`' |
// '\u001c'; IND_MIC_E_OLD: '\'' | '\u001d';

// // User-defined data type indicator IND_USER_DEFINED: '{';

// // Third-party traffic data type indicator IND_THIRD_PARTY: '}';

// // ================ POSITION REPORTS ================

// POSITION_REPORT: (IND_POS POS_REP_BODY) | (IND_POS_TIMESTAMP TIMESTAMP POS_REP_BODY);

// POS_REP_BODY: (POS_UNCOMPRESSED | POS_COMPRESSED) POS_COMMENT?;

// POS_UNCOMPRESSED: LAT SYMBOL_TABLE LON SYMBOL_CODE;

// LAT: LAT_DD LAT_MM '.' LAT_mm NORTH_OR_SOUTH; LAT_DD: FROM_00_TO_89; LAT_MM: FROM_00_TO_59;
// LAT_mm: FROM_00_TO_99;

// LON: LON_DDD LAT_MM '.' LAT_mm WEST_OR_EAST; LON_DDD: FROM_000_TO_179; LON_MM: FROM_00_TO_59;
// LON_mm: FROM_00_TO_99;

// POS_COMPRESSED: SYMBOL_TABLE LAT_CMP LON_CMP SYMBOL_CODE POS_CMP_EXT POS_CMP_TYPE;

// LAT_CMP: BASE_91 BASE_91 BASE_91 BASE_91; LON_CMP: BASE_91 BASE_91 BASE_91 BASE_91;

// POS_CMP_EXT: ANY ANY; POS_CMP_TYPE: ANY;

// POS_COMMENT: ANY+;

// // ==================== TIMESTAMP ====================

// TIMESTAMP: TS_DHM | TS_HMS | TS_MDHM;

// TS_DHM: FROM_00_TO_31 FROM_00_TO_23 FROM_00_TO_59 TS_DHM_ZONE; TS_DHM_ZONE: TS_DHM_ZONE_UTC |
// TS_DHM_ZONE_LOCAL; TS_DHM_ZONE_UTC: 'z'; TS_DHM_ZONE_LOCAL: '/';

// TS_HMS: FROM_00_TO_23 FROM_00_TO_59 FROM_00_TO_59 TS_HMS_ZONE; TS_HMS_ZONE: 'h';

// TS_MDHM: FROM_01_TO_12 FROM_00_TO_31 FROM_00_TO_23 FROM_00_TO_59;

// // ==================== STATUS REPORTS ====================

// STATUS_REPORT: IND_STATUS TS_DHM? ANY*;

// // ==================== MESSAGES ====================

// MESSAGE: IND_MESSAGE (MSG_REGULAR | MSG_BULLETIN);

// MSG_REGULAR: MAX_9_ANY_RIGHT_PADDED IND_MESSAGE MSG_REGULAR_BODY; MSG_BULLETIN: 'BLN' ANY
// MAX_5_ANY_RIGHT_PADDED IND_MESSAGE MSG_TEXT;

// MSG_REGULAR_BODY: (MSG_TEXT MSG_NUMBER?) | MSG_ACK_BODY | MSG_REJ_BODY;

// MSG_TEXT: ANY+; MSG_NUMBER: '{' MSG_NUMBER_DIGITS; MSG_NUMBER_DIGITS: DIGIT DIGIT? DIGIT? DIGIT?
// DIGIT?;

// MSG_ACK_BODY: IND_MSG_ACK MSG_NUMBER; IND_MSG_ACK: 'ack';

// MSG_REJ_BODY: IND_MSG_REJ MSG_NUMBER; IND_MSG_REJ: 'rej';

// // ==================== OBJECTS ====================

// OBJECT: IND_OBJECT OBJ_NAME IND_OBJ_LIVE_KILLED OBJ_TIMESTAMP POS_REP_BODY;

// OBJ_NAME: MAX_9_ANY_RIGHT_PADDED; OBJ_TIMESTAMP: TS_DHM | TS_HMS;

// IND_OBJ_LIVE_KILLED: IND_OBJ_LIVE | IND_OBJ_KILLED; IND_OBJ_LIVE: '*'; IND_OBJ_KILLED: '_';

// // ==================== ITEMS ====================

// ITEM: IND_ITEM MIN_3_MAX_9_ANY IND_ITEM_LIVE_KILLED POS_REP_BODY;

// IND_ITEM_LIVE_KILLED: IND_ITEM_LIVE | IND_ITEM_KILLED; IND_ITEM_LIVE: '!'; IND_ITEM_KILLED: ')';

// // ==================== STATION CAPABILITIES ====================

// CAPABILITIES: IND_CAPABILITIES ANY+;

// // ==================== QUERIES ====================

// QUERY: IND_QUERY ANY*;

// // ==================== TELEMETRY ====================

// TELEMETRY: IND_TELEMETRY ANY+;

// // ==================== WEATHER ====================

// WEATHER: IND_WEATHER ANY+;

// // ==================== POSITIONLESS WEATHER ====================

// WEATHER_POSITIONLESS: IND_WEATHER_POSITIONLESS ANY+;

// // ==================== MIC-E ====================

// MIC_E: IND_MIC_E ANY*;

// // ==================== USER-DEFINED ====================

// USER_DEFINED: IND_USER_DEFINED UD_USER_ID UD_PACKET_TYPE ANY*; UD_USER_ID: ANY; UD_PACKET_TYPE:
// ANY;

// // ==================== THIRD-PARTY TRAFFIC ====================

// THIRD_PARTY: IND_THIRD_PARTY ANY*;

// ==================== UTILITY RULES ====================

// BASE_91: [!-{]; ALPHANUM: [A-Za-z0-9]; ALPHANUM_OR_DASH: [A-Za-z0-9-]; DIGIT: [0-9]; SPACE: ' ';

// SYMBOL_TABLE: [A-Z/\\]; SYMBOL_CODE: ANY; NORTH_OR_SOUTH: [NS]; WEST_OR_EAST: [WE];
// FROM_01_TO_12: ('0' [1-9]) | ('1' [0-2]); FROM_00_TO_23: ([0-1][0-9]) | ('2' [0-3]);
// FROM_00_TO_31: ([0-2][0-9]) | ('3' [0-1]); FROM_00_TO_59: [0-5][0-9]; FROM_00_TO_89: [0-8][0-9];
// FROM_00_TO_99: [0-9][0-9]; FROM_000_TO_179: ('0' FROM_00_TO_99) | ('1' [0-7] [0-9]);

// MIN_3_MAX_9_ANY: ANY ANY ANY ANY? ANY? ANY? ANY? ANY? ANY?;

// MAX_5_ANY_RIGHT_PADDED: (SPACE SPACE SPACE SPACE SPACE) | (ANY SPACE SPACE SPACE SPACE) | (ANY
// ANY SPACE SPACE SPACE) | (ANY ANY ANY SPACE SPACE) | (ANY ANY ANY ANY SPACE) | (ANY ANY ANY ANY
// ANY);

// MAX_9_ANY_RIGHT_PADDED: (SPACE SPACE SPACE SPACE SPACE SPACE SPACE SPACE SPACE) | (ANY SPACE
// SPACE SPACE SPACE SPACE SPACE SPACE SPACE) | (ANY ANY SPACE SPACE SPACE SPACE SPACE SPACE SPACE)
// | (ANY ANY ANY SPACE SPACE SPACE SPACE SPACE SPACE) | (ANY ANY ANY ANY SPACE SPACE SPACE SPACE
// SPACE) | (ANY ANY ANY ANY ANY SPACE SPACE SPACE SPACE) | (ANY ANY ANY ANY ANY ANY SPACE SPACE
// SPACE) | (ANY ANY ANY ANY ANY ANY ANY SPACE SPACE) | (ANY ANY ANY ANY ANY ANY ANY ANY SPACE) |
// (ANY ANY ANY ANY ANY ANY ANY ANY ANY);