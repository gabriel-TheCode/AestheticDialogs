# AestheticDialogs ships no reflection, no serialization and no resource lookups
# by name, so consumers need no extra keep rules. Compose's own consumer rules
# (shipped by androidx.compose.*) already cover the runtime.
#
# This file is intentionally empty: adding blanket `-keep class
# com.thecode.aestheticdialogs.**` rules would prevent R8 from shrinking unused
# dialogs out of consumer apps, which is the opposite of what a UI library
# should do.
