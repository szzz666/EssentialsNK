package cn.yescallop.essentialsnk.command.defaults.home;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.level.Location;
import cn.nukkit.utils.TextFormat;
import cn.yescallop.essentialsnk.EssentialsAPI;
import cn.yescallop.essentialsnk.Language;
import cn.yescallop.essentialsnk.command.CommandBase;

import static cn.yescallop.essentialsnk.util.taskUtil.Async;

public class HomeCommand extends CommandBase {

    public HomeCommand(EssentialsAPI api) {
        super("home", api);
        this.setAliases(new String[]{"homes"});

        // command parameters
        commandParameters.clear();
        this.commandParameters.put("default", new CommandParameter[]{
                new CommandParameter("home", CommandParamType.TEXT, true)
        });
    }

    public boolean execute(CommandSender sender, String label, String[] args) {
        Async(() -> {
            if (!this.testPermission(sender)) {
                return;
            }
            if (!this.testIngame(sender)) {
                return;
            }
            if (args.length > 1) {
                this.sendUsage(sender);
                return;
            }
            Player player = (Player) sender;
            if (args.length == 0) {
                String[] list = api.getHomesList(player);
                if (list.length == 0) {
                    sender.sendMessage(TextFormat.RED + Language.translate("commands.home.nohome"));
                    return;
                }
                sender.sendMessage(Language.translate("commands.home.list") + "\n" + String.join(", ", list));
                return;
            }
            Location home = api.getHome(player, args[0].toLowerCase());
            if (home == null) {
                sender.sendMessage(TextFormat.RED + Language.translate("commands.home.notexists", args[0]));
                return;
            }
            player.teleport(home);
            sender.sendMessage(Language.translate("commands.home.success", args[0]));
        });
        return true;
    }
}
