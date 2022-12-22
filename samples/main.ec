include "settings.ec";
include "series.ec";

-- do something here
println("ec files loaded");

a = .3;
println(a);

println(settings.background.color);

s = "The rain in spain " +
    "falls mainly on the " + 
    "plain.";

println(s);