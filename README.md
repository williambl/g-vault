# GVault

A Gunpowder add-on which adds *vaults*, per-player end-chest-style inventories which can be accessed via commands.

## Commands

| Command                           | Permission node    | Action                                   |
|-----------------------------------|--------------------|------------------------------------------|
| `/vault <number>`                 | `gvault.viewVault` | Shows vault `#number` to yourself.       |
| `/vault showto <target> <number>` | `gvault.showTo`    | Shows `target` their own vault `#number` |
| `/vault spy <target> <number>`    | `gvault.spy`       | Shows you `target`'s vault `#number`     |

## Config

```yaml
vaultCount: 3 # How many vaults does each player have access to?
isDoubleChest: no # Are vaults double-size?
configGroups: [] # A list of Config Groups
```

## Config Groups

A config group looks like:
```yaml
name: special
vaultCount: 5
doubleChest: yes
```

Then, if a player has the `gvault.group.special`, they will have 5 double-size vaults.