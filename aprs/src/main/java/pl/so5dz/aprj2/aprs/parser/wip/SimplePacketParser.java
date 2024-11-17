package pl.so5dz.aprj2.aprs.parser.wip;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import pl.so5dz.aprj2.aprs.antlr.PacketLexer;
import pl.so5dz.aprj2.aprs.antlr.PacketParser;
import pl.so5dz.aprj2.aprs.models.Packet;

public class SimplePacketParser {
    private static ParseTreeWalker parseTreeWalker = ParseTreeWalker.DEFAULT;
    private static PacketFactory packetFactory = new PacketFactory();

    public Packet parse(String packetString) {
        if (packetString == null || packetString.isBlank()) {
            return null;
        }
        CharStream inputStream = CharStreams.fromString(packetString);
        PacketLexer lexer = new PacketLexer(inputStream);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        PacketParser parser = new PacketParser(tokens);
        ParseTree tree = parser.packet();
        parseTreeWalker.walk(packetFactory, tree);
        return packetFactory.getPacket();
    }
}
