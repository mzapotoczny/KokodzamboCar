package pl.wroc.uni.ii.kokodzambocar.OBD.Loggers;

import java.util.HashMap;

import pl.wroc.uni.ii.kokodzambocar.OBD.Commands.Engine.OBDEngineLoadCommand;
import pl.wroc.uni.ii.kokodzambocar.OBD.Commands.Engine.OBDEngineRuntimeCommand;
import pl.wroc.uni.ii.kokodzambocar.OBD.Commands.Engine.OBDMAFCommand;
import pl.wroc.uni.ii.kokodzambocar.OBD.Commands.Engine.OBDRPMCommand;
import pl.wroc.uni.ii.kokodzambocar.OBD.Commands.Engine.OBDThrottlePositionCommand;
import pl.wroc.uni.ii.kokodzambocar.OBD.Commands.OBDCommand;
import pl.wroc.uni.ii.kokodzambocar.OBD.Commands.Proto.OBDAutoProtocolCommand;
import pl.wroc.uni.ii.kokodzambocar.OBD.Commands.Proto.OBDEchoOffCommand;
import pl.wroc.uni.ii.kokodzambocar.OBD.Commands.Proto.OBDLineFeedOffCommand;
import pl.wroc.uni.ii.kokodzambocar.OBD.Commands.Proto.OBDResetCommand;
import pl.wroc.uni.ii.kokodzambocar.OBD.Commands.Proto.OBDSupportedPIDsCommand;
import pl.wroc.uni.ii.kokodzambocar.OBD.Commands.Temperature.OBDAirIntakeTemperatureCommand;
import pl.wroc.uni.ii.kokodzambocar.OBD.Commands.Temperature.OBDAmbientAirTemperature;
import pl.wroc.uni.ii.kokodzambocar.OBD.Commands.Temperature.OBDEngineCoolantTemperature;

/**
 * Created by michal on 10.11.14.
 */
public class OBDId {
    private static boolean mIdsLoaded = false;
    private static HashMap<Class<?>, Integer> mCommandsIds =
            new HashMap<Class<?>, Integer>();

    private static void loadIds(){
        // Engine
        mCommandsIds.put(OBDEngineLoadCommand.class, 1);
        mCommandsIds.put(OBDEngineRuntimeCommand.class, 2);
        mCommandsIds.put(OBDMAFCommand.class, 3);
        mCommandsIds.put(OBDRPMCommand.class, 4);
        mCommandsIds.put(OBDThrottlePositionCommand.class, 5);
        // Proto
        mCommandsIds.put(OBDAutoProtocolCommand.class, 20);
        mCommandsIds.put(OBDEchoOffCommand.class, 21);
        mCommandsIds.put(OBDLineFeedOffCommand.class, 22);
        mCommandsIds.put(OBDResetCommand.class, 23);
        mCommandsIds.put(OBDSupportedPIDsCommand.class, 24);
        // Temperature
        mCommandsIds.put(OBDAirIntakeTemperatureCommand.class, 40);
        mCommandsIds.put(OBDAmbientAirTemperature.class, 41);
        mCommandsIds.put(OBDEngineCoolantTemperature.class, 42);

        mIdsLoaded = true;
    }

    public static Integer getId(OBDCommand command){
        if (!mIdsLoaded){
            loadIds();
        }
        return mCommandsIds.get(command.getClass());
    }
}
