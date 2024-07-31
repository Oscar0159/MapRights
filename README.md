# Map Rights
A Minecraft plugin that allows players to sign maps, protecting their creations from unauthorized copying.

# Config.yml
```yaml
# The language of the plugin, currently only 【en_US】 and 【zh_TW】 are supported
# If you want to add a new language, please create a new file in the lang folder
language: en_US
```

# Commands
| Command  | Aliases  | Description  |
| ------------ | ------------ | ------------ |
| /maprights sign |  /mr sign | Sign a map to protect it from unauthorized copying |
| /maprights unsign  | /mr unsign  | Unsign a map, allowing it to be copied again |

# Permission
| Permission  | Default  | Description  |
| ------------ | ------------ | ------------ |
| maprights.use  | true  |  Allows the player to use the /maprights command |
| maprights.sign  |  true | Allows the player to sign a map  |
| maprights.unsign  |  true |  Allows the player to unsign a map |

# TODO
- [ ] Add Vault support