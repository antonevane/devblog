---
layout:     post
title:      "Files I/O NIO Way"
subtitle:   "New efficient API to work with file system and files"
date:       2015-04-23 03:32:10
author:     "Silence of Ninja"
tags : [java, core, files, io, nio]
header-img: "img/post-bg-03.jpg"
---

Since Java 7 a bit dusty `I/O` becomes a burden and new package `java.nio.file` has been introduced in the language. 
It defines classes to access files and file system. 

<h4 class="section-heading">Path</h4>
The Path is a programmatic representation of a path in the file system separated by a specific separator. 
Path might have `root`. Root is file store hierarchical starting point. It representation is `/` in Unix. 
Windows might have multiply roots, and usually it has `C:\` root.
You can easily create a `Path` by using using `get` methods from the `Files` class. 
{% highlight java %}
 Path p1 = Paths.get("/tmp/");
 // Path to the Root
 Path p2 = Paths.get("/");

 Path p3 = Paths.get("C:\\Windows");
 Path p4 = Paths.get("C:", "Windows");
 // p3 and p4 are identical
{% endhighlight %}

Path might be represented as URI: 
{% highlight java %}
Path uriPath = Paths.get(URI.create("file:///Users"));
{% endhighlight %}

The longest version for the following code:

{% highlight java %}
// FileSystems.getDefault() - returns default file system
Path path = FileSystems.getDefault().getPath("/tmp/");
{% endhighlight %}

All previous examples start from the root. Basically all of them might represent an `absolute path` (path in the file system).
Path interface has 2 methods to convert relative path to the 'absolute path'. <a href="#reference">Reference.<a/>

 `Path toAbsolutePath()` - Returns a Path object representing the absolute path of this path.
 
 `Path toRealPath(LinkOption... options) throws IOException` - 
 Returns an absolute path represent the real path of the file located by this object. 
 Options indicating how symbolic links are handled

{% highlight java %}
// FileSystems.getDefault() - returns default file system
Path path = Paths.get("usr");
path.toAbsolutePath();
  
// the unix output might look like '/usr'
{% endhighlight %}

If you pass `null` into the Files.get(...)` it will throw `java.lang.NullPointerException`

<h4 class="section-heading">Files</h4>
New API introduces new `Files` class. It contains many convenient methods to work with files. `Files`
 works with the `Path` data representation. Lets start look into some of them.

If you have a very small file you can read it into the `byte[]` or `String` you can use the 
readAllBytes(Path) or readAllLines(Path, Charset) method.

{% highlight java %}
@Test
public void crudNioFilesAPI() throws IOException {
    // "user.dir" - User working directory
    String workingDirectory = System.getProperty("user.dir");
    Path path = Paths.get(workingDirectory, "test.txt");

    // Creates file 'test.txt' in the 'user.dir'
    Files.createFile(path);

    // In the real life example it is better to use
    // TETS_FILE_PAYLOAD.getBytes(StandardCharsets.UTF_8)
    // You can get system encoding - System.getProperty("file.encoding");
    byte[] data = TETS_FILE_PAYLOAD.getBytes();
    // Writes payload to the created file
    Files.write(path, data);

    // Reads data from the file
    byte[] readData = Files.readAllBytes(path);

    String stringData = new String(readData);

    assertEquals("Checks if data are the same", TETS_FILE_PAYLOAD, stringData);

    // Deletes file and returns true if the file exists
    boolean deleted = Files.deleteIfExists(path);

    assertTrue(deleted);
}   
{% endhighlight %}

The following example demonstrates only a tip of iceberg. 

<h4 class="section-heading">Legacy File I/O</h4>
The Files class has a number of method to support capabilities between Legacy I/0 and NIO.
it provides methods with signature `Files.new....`, For example `Files.newBufferedWriter`, Files.newBufferedReader.

{% highlight java %}
@Test
public void crudLegacyFilesAPI() throws IOException {
    // "user.dir" - User working directory
    String workingDirectory = System.getProperty("user.dir");
    Path path = Paths.get(workingDirectory, "test.txt");

    // Creates file 'test.txt' in the 'user.dir'
    Files.createFile(path);

    // Writes payload to file
    try (BufferedWriter bw = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
        bw.write(TETS_FILE_PAYLOAD);
    } catch (IOException ioex) {
        fail(ioex.getMessage());
    }

    // Reads data from the file
    StringBuilder stringData = new StringBuilder();
    try (BufferedReader br = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
        String line;
        while ((line = br.readLine()) != null) {
            stringData.append(line);
        }
    } catch (IOException ioex) {
        fail(ioex.getMessage());
    }

    assertEquals("Checks if data are the same", TETS_FILE_PAYLOAD, stringData.toString());

    // Deletes file and returns true if the file exists
    boolean deleted = Files.deleteIfExists(path);

    assertTrue(deleted);
}    
{% endhighlight %}

The `Path` has method `toPath()` to convert file to path at the same time `File` 
contains a similar method `toPath()` to support `Path`. 

<h4 class="section-heading" id="reference">Reference</h4>
Path, Absolute path, Relative path conceptions you can find on the [wiki](http://en.wikipedia.org/wiki/Path_%28computing%29).

The Following are useful methods of the [Files](http://docs.oracle.com/javase/8/docs/api/java/nio/file/Files.html) class.

  `static FileTime	getLastModifiedTime(Path path, LinkOption... options)` - Returns a file's last modified time.
  
  `static UserPrincipal	getOwner(Path path, LinkOption... options)` - Returns the owner of a file.

  `static Path	copy(Path source, Path target, CopyOption... options)` - Copy a file to a target file. 

You can find more details inside [package javadoc](http://docs.oracle.com/javase/8/docs/api/java/nio/file/package-summary.html).
